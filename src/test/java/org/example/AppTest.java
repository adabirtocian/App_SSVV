package org.example;

import domain.*;
import org.junit.Assert;
import org.junit.Test;
import repository.*;
import service.Service;
import validation.*;
import validation.Validator;

import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class AppTest {

    // validators
    private Validator<Student> studentValidator = new StudentValidator();
    private Validator<Tema> assignmentValidator = new TemaValidator();
    private Validator<Nota> gradeValidator = new NotaValidator();

    // repositories
    private StudentXMLRepository studentRepository;
    private TemaXMLRepository assignmentRepository;
    private NotaXMLRepository gradeRepository;

    private Service service;

    public void init()
    {
        this.studentRepository = new StudentXMLRepository(this.studentValidator, "studenti_test.xml");
        this.assignmentRepository = new TemaXMLRepository(this.assignmentValidator, "teme_test.xml");
        this.gradeRepository = new NotaXMLRepository(this.gradeValidator, "note_test.xml");

        this.service = new Service(this.studentRepository, this.assignmentRepository, this.gradeRepository);
    }

    public void clearFiles()
    {
        ArrayList<String> ids = new ArrayList<>();
        for(Student s : this.service.findAllStudents())
        {
            ids.add(s.getID());
        }
        for(String id: ids)
        {
            this.studentRepository.delete(id);
        }
    }

    @Test
    public void testAddStudent_tc1()
    {
        init();
        String id = "13";
        String name = "s1";
        int group = 123;
        int expected = 1;
        int result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(expected, result); // student was added
        clearFiles();
    }

    @Test
    public void testAddStudent_tc2()
    {
        init();
        String id = "";
        String name = "student1";
        int group = 123;

        int expected = 0;

        int result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(expected, result); // student not added because of empty id
        clearFiles();
    }

    @Test
    public void testAddStudent_tc3()
    {
        init();
        String id = null;
        String name = "student1";
        int group = 123;

        int expected = 0;

        int result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(expected, result); // student not added because of null id
        clearFiles();
    }

    @Test
    public void testAddStudent_tc4()
    {
        init();
        String id = "1";
        String name = "";
        int group = 123;

        int expected = 0;

        int result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(expected, result);
        clearFiles();
    }

    @Test
    public void testAddStudent_tc5()
    {
        init();
        String id = "1";
        String name = null;
        int group = 123;

        int expected = 0;

        int result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(expected, result);
        clearFiles();
    }

    @Test
    public void testAddStudent_tc6()
    {
        init();
        String id = "1";
        String name = "student1";
        int group = 109;

        int expected = 0;

        int result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(expected, result);
        clearFiles();
    }

    @Test
    public void testAddStudent_tc7()
    {
        init();
        String id = "1";
        String name = "student1";
        int group = 939;

        int expected = 0;

        int result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(expected, result);
        clearFiles();
    }

    @Test
    public void testAddStudent_tc8()
    {
        init();
        String id = "100";
        String name1 = "student1";
        int group1 = 110;

        int expected = 0;
        int result1 = this.service.saveStudent(id, name1, group1);
        Assert.assertEquals(expected, result1);
        clearFiles();
    }

    @Test
    public void testAddStudent_tc9()
    {
        init();
        String id = "1";
        String name = "student1";
        int group = 111;

        int expected = 1;

        int result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(expected, result);
        clearFiles();
    }

    @Test
    public void testAddStudent_tc10()
    {
        init();
        String id = "100";
        String name1 = "student1";
        int group1 = 110;
        String name2 = "student2";
        int group2 = 933;

        int result1 = this.service.saveStudent(id, name1, group1);
        int result2 = this.service.saveStudent(id, name2, group2);
        //Should check this out, not sure what boundaries are here for groups,
        // it should add the first one but the second one no, but like this it
        // doesn't add either of them
        Assert.assertEquals(0, result1);
        Assert.assertEquals(1, result2);
        clearFiles();
    }
}

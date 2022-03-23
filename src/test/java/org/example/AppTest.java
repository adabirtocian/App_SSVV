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

    public void clearStudentFiles()
    {
        ArrayList<String> ids_students = new ArrayList<>();

        for(Student s : this.service.findAllStudents())
        {
            ids_students.add(s.getID());
        }
        for(String id: ids_students)
        {
            this.studentRepository.delete(id);
        }
    }

    public void clearAssignmentFiles()
    {
        ArrayList<String> ids_assignments = new ArrayList<>();

        for(Tema a : this.service.findAllTeme())
        {
            ids_assignments.add(a.getID());
        }
        for(String id: ids_assignments)
        {
            this.assignmentRepository.delete(id);
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
        clearStudentFiles();
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
        clearStudentFiles();
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
        clearStudentFiles();
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
        Assert.assertEquals(expected, result); // student not added bc of empty name
        clearStudentFiles();
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
        Assert.assertEquals(expected, result); // student not added bc of null name
        clearStudentFiles();
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
        Assert.assertEquals(expected, result); // student not added bc of gr number <110
        clearStudentFiles();
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
        Assert.assertEquals(expected, result);// student not added bc of gr number >=938
        clearStudentFiles();
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
        Assert.assertEquals(expected, result1);// student not added bc of gr number <=110
        clearStudentFiles();
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
        Assert.assertEquals(expected, result);// student added bc of gr number >110
        clearStudentFiles();
    }

    @Test
    public void testAddStudent_tc10()
    {
        init();
        String id = "100";
        String name1 = "student1";
        int group1 = 111;
        String name2 = "student2";
        int group2 = 933;

        int result1 = this.service.saveStudent(id, name1, group1);
        int result2 = this.service.saveStudent(id, name2, group2);
        // First student added because of valid properties
        Assert.assertEquals(1, result1);
        // Second student not added because of already existent id(but ok other properties)
        Assert.assertEquals(0, result2);
        clearStudentFiles();
    }

    @Test
    public void testAddStudent_tc11()
    {
        init();
        int count = 0;
        int expected_size = 0;
        for(Student s: this.service.findAllStudents())
        {
            count+=1;
        }
        Assert.assertEquals(expected_size, count);

        String id = "100";
        String name = "student";
        int group = 931;
        int expected = 1;
        int result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(expected, result); // student added
        count = 0;
        expected_size = 1;
        for(Student s: this.service.findAllStudents())
        {
            count+=1;
        }
        Assert.assertEquals(expected_size, count);
        clearStudentFiles();
    }

    @Test
    public void testAddStudent_tc12()
    {
        init();
        int count = 0;
        int expected_size = 0;
        for(Student s: this.service.findAllStudents())
        {
            count+=1;
        }
        Assert.assertEquals(expected_size, count);

        String id = "";
        String name = "student";
        int group = 931;
        int expected = 0;
        int result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(expected, result); // student not added

        count = 0;
        for(Student s: this.service.findAllStudents())
        {
            count+=1;
        }
        Assert.assertEquals(expected_size, count);
        clearStudentFiles();
    }

    @Test
    public void testAddAssignment_tc1()
    {
        init();
        String id = "1";
        String description = "d1";
        int deadline = 3;
        int startline = 2;

        int expected = 1;

        int result = this.service.saveTema(id, description, deadline, startline);
        Assert.assertEquals(expected, result); // assignment was added
        clearAssignmentFiles();
    }

    @Test
    public void testAddAssignment_tc2()
    {
        init();
        String id = "1";
        String description = "d1";

        int deadline = 20;
        int startline = 2;
        int expected = 0;

        int result = this.service.saveTema(id, description, deadline, startline);
        Assert.assertEquals(expected, result); // assignment not added because of deadline
        clearAssignmentFiles();
    }
}

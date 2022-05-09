package org.example;

import domain.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import repository.*;
import service.Service;
import validation.*;
import validation.Validator;
import java.util.ArrayList;
import java.util.Objects;

public class IntegrationTest {
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

    public void clearGradeFiles()
    {
        ArrayList<Pair<String, String>> ids_grades = new ArrayList<>();
        for(Nota n: this.service.findAllNote())
        {
            ids_grades.add(n.getID());
        }
        for(Pair<String, String> p: ids_grades)
        {
            this.gradeRepository.delete(p);
        }
    }

    @Test
    public void test_addGrade()
    {
        init();
        this.service.saveStudent("id1", "s1", 931);
        this.service.saveTema("id1", "d1", 7, 3);

        int result = this.service.saveNota("id1", "id1", 7, 9, "ok");
        Assert.assertEquals(1, result);
        for(Nota n: this.service.findAllNote())
        {
            if(Objects.equals(n.getID().getObject1(), "id1") && Objects.equals(n.getID().getObject2(), "id1"))
            {
                assert(n.getNota() == 2);
                break;
            }
        }

        clearAssignmentFiles();
        clearStudentFiles();
        clearGradeFiles();
    }

    @Test
    public void test_addStudent()
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
    public void test_addAssignment()
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
    public void test_integration()
    {
        init();
        String id_assignment = "id1";
        String description = "d1";
        int deadline = 3;
        int startline = 2;

        int result = this.service.saveTema(id_assignment, description, deadline, startline);
        Assert.assertEquals(1, result); // assignment was added

        String id = "id1";
        String name = "s1";
        int group = 123;

        result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(1, result); // student was added

        result = this.service.saveNota("id1", "id1", 7, 9, "ok");
        Assert.assertEquals(1, result);

        clearAssignmentFiles();
        clearStudentFiles();
        clearGradeFiles();
    }

    @Test
    public void test_addStudent_addAssignment()
    {
        init();
        String id = "13";
        String name = "s1";
        int group = 123;
        int expected_student = 1;
        int result_student = this.service.saveStudent(id, name, group);

        id = "1";
        String description = "d1";
        int deadline = 3;
        int startline = 2;
        int expected_assignment = 1;
        int result_assignment = this.service.saveTema(id, description, deadline, startline);

        Assert.assertEquals(expected_student, result_student); // student was added
        Assert.assertEquals(expected_assignment, result_assignment); // assignment was added
        clearAssignmentFiles();
        clearStudentFiles();
    }

    @Test
    public void test_addStudent_addAssignment_addGrade()
    {
        init();
        String id_student = "13";
        String name = "s1";
        int group = 123;
        int expected_student = 1;
        int result_student = this.service.saveStudent(id_student, name, group);

        String id_assignment = "1";
        String description = "d1";
        int deadline = 3;
        int startline = 2;
        int expected_assignment = 1;
        int result_assignment = this.service.saveTema(id_assignment, description, deadline, startline);

        int result = this.service.saveNota(id_student, id_assignment, 7, 9, "ok");
        Assert.assertEquals(1, result);
        Assert.assertEquals(expected_student, result_student); // student was added
        Assert.assertEquals(expected_assignment, result_assignment); // assignment was added

        clearAssignmentFiles();
        clearStudentFiles();
        clearGradeFiles();
    }

}

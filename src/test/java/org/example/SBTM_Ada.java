package org.example;

import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.*;

import java.util.ArrayList;

public class SBTM_Ada {
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
    public void test_addGrade_noStudent()
    {
        init();
        String id = "id1";
        String name = "s1";
        int group = 123;

        int result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(1, result); // student was added

        result = this.service.saveNota("id1", "id1", 7, 9, "ok");
        Assert.assertEquals(-1, result);

        clearAssignmentFiles();
        clearStudentFiles();
        clearGradeFiles();
    }

    @Test
    public void test_addGrade_noAssignment()
    {
        init();
        String id_assignment = "id1";
        String description = "d1";
        int deadline = 3;
        int startline = 2;

        int result = this.service.saveTema(id_assignment, description, deadline, startline);
        Assert.assertEquals(1, result); // assignment was added

        result = this.service.saveNota("id1", "id1", 7, 9, "ok");
        Assert.assertEquals(-1, result);

        clearAssignmentFiles();
        clearStudentFiles();
        clearGradeFiles();
    }

    @Test
    public void test_addGrade_smallGrade()
    {
        init();
        String id_assignment = "id1";
        String description = "d1";
        int deadline = 3;
        int startline = 2;
        int result = this.service.saveTema(id_assignment, description, deadline, startline);
        Assert.assertEquals(1, result); // assignment was added

        String id_student = "id1";
        String name = "s1";
        int group = 123;
        result = this.service.saveStudent(id_student, name, group);
        Assert.assertEquals(1, result); // student was added

        result = this.service.saveNota("id1", "id1", 2, 4, "bad");
        Assert.assertEquals(0, result); // grade less than 0

        Nota grade = new Nota(new Pair(id_student, id_assignment), -0.5, 4, "bad");
        Assertions.assertThrows(ValidationException.class, () -> { this.gradeValidator.validate(grade);});

        clearAssignmentFiles();
        clearStudentFiles();
        clearGradeFiles();
    }

    @Test
    public void test_addGrade_turnedInLate()
    {
        init();
        String id_assignment = "id1";
        String description = "d1";
        int deadline = 3;
        int startline = 2;
        int result = this.service.saveTema(id_assignment, description, deadline, startline);
        Assert.assertEquals(1, result); // assignment was added

        String id_student = "id1";
        String name = "s1";
        int group = 123;
        result = this.service.saveStudent(id_student, name, group);
        Assert.assertEquals(1, result); // student was added

        result = this.service.saveNota("id1", "id1", 7, 6, "ok");
        Assert.assertEquals(1, result);

        Nota grade = this.gradeRepository.findOne(new Pair<String, String>(id_student, id_assignment));
        Assert.assertEquals(1.0, grade.getNota(), 0.1);

        clearAssignmentFiles();
        clearStudentFiles();
        clearGradeFiles();
    }

    @Test
    public void test_addGrade_grade10()
    {
        init();
        String id_assignment = "id1";
        String description = "d1";
        int deadline = 3;
        int startline = 2;
        int result = this.service.saveTema(id_assignment, description, deadline, startline);
        Assert.assertEquals(1, result); // assignment was added

        String id_student = "id1";
        String name = "s1";
        int group = 123;
        result = this.service.saveStudent(id_student, name, group);
        Assert.assertEquals(1, result); // student was added

        result = this.service.saveNota("id1", "id1", 10, 3, "good");
        Assert.assertEquals(1, result);

        Nota grade = this.gradeRepository.findOne(new Pair<String, String>(id_student, id_assignment));
        Assert.assertEquals(10.0, grade.getNota(), 0.1);

        clearAssignmentFiles();
        clearStudentFiles();
        clearGradeFiles();
    }

    @Test
    public void test_addGrade_grade11()
    {
        init();
        String id_assignment = "id1";
        String description = "d1";
        int deadline = 3;
        int startline = 2;
        int result = this.service.saveTema(id_assignment, description, deadline, startline);
        Assert.assertEquals(1, result); // assignment was added

        String id_student = "id1";
        String name = "s1";
        int group = 123;
        result = this.service.saveStudent(id_student, name, group);
        Assert.assertEquals(1, result); // student was added

        result = this.service.saveNota("id1", "id1", 11, 3, "good");
        Assert.assertEquals(0, result);

        clearAssignmentFiles();
        clearStudentFiles();
        clearGradeFiles();
    }

    @Test
    public void test_addGrade_grade0()
    {
        init();
        String id_assignment = "id1";
        String description = "d1";
        int deadline = 3;
        int startline = 2;
        int result = this.service.saveTema(id_assignment, description, deadline, startline);
        Assert.assertEquals(1, result); // assignment was added

        String id_student = "id1";
        String name = "s1";
        int group = 123;
        result = this.service.saveStudent(id_student, name, group);
        Assert.assertEquals(1, result); // student was added

        result = this.service.saveNota("id1", "id1", 2.5, 4, "bad");
        Assert.assertEquals(1, result);

        Nota grade = this.gradeRepository.findOne(new Pair<String, String>(id_student, id_assignment));
        Assert.assertEquals(0.0, grade.getNota(), 0.1);

        clearAssignmentFiles();
        clearStudentFiles();
        clearGradeFiles();
    }

    @Test
    public void test_addGrade_valNota11()
    {
        init();
        String id_assignment = "id1";
        String description = "d1";
        int deadline = 3;
        int startline = 2;
        int result = this.service.saveTema(id_assignment, description, deadline, startline);
        Assert.assertEquals(1, result); // assignment was added

        String id_student = "id1";
        String name = "s1";
        int group = 123;
        result = this.service.saveStudent(id_student, name, group);
        Assert.assertEquals(1, result); // student was added

        result = this.service.saveNota("id1", "id1", 11, 4, "good");
        Assert.assertEquals(1, result);

        Nota grade = this.gradeRepository.findOne(new Pair<String, String>(id_student, id_assignment));
        Assert.assertEquals(8.5, grade.getNota(), 0.1);

        clearAssignmentFiles();
        clearStudentFiles();
        clearGradeFiles();
    }

    @Test
    public void test_addGrade_turnedInWeek15()
    {
        init();
        String id_assignment = "id1";
        String description = "d1";
        int deadline = 3;
        int startline = 2;
        int result = this.service.saveTema(id_assignment, description, deadline, startline);
        Assert.assertEquals(1, result); // assignment was added

        String id_student = "id1";
        String name = "s1";
        int group = 123;
        result = this.service.saveStudent(id_student, name, group);
        Assert.assertEquals(1, result); // student was added

        result = this.service.saveNota("id1", "id1", 11, 15, "good");
        Assert.assertEquals(1, result);

        Nota grade = this.gradeRepository.findOne(new Pair<String, String>(id_student, id_assignment));
        Assert.assertEquals(1.0, grade.getNota(), 0.1);

        clearAssignmentFiles();
        clearStudentFiles();
        clearGradeFiles();
    }
}

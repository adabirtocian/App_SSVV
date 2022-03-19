package org.example;

import domain.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import repository.*;
import service.Service;
import validation.*;
import validation.Validator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;

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
        for(Student s : this.service.findAllStudents())
        {
            this.studentRepository.delete(s.getID());
        }
    }

    @Test
    public void testAddStudent_tc1()
    {
        init();
        String id = "13";
        String name = "s1";
        int group = 123;

        int result = this.service.saveStudent(id, name, group);
        Assert.assertEquals(result, 1);
        clearFiles();
    }
}

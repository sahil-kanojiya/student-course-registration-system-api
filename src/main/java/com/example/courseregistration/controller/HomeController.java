package com.example.courseregistration.controller;

import com.example.courseregistration.dao.CourseDao;
import com.example.courseregistration.dao.StudentDao;
import com.example.courseregistration.dto.RegisterDto;
import com.example.courseregistration.entity.Course;
import com.example.courseregistration.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StudentDao studentDao;
    private final CourseDao courseDao;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        model.addAttribute("courses", courseDao.findAll());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerDto") RegisterDto registerDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseDao.findAll());
            return "register";
        }

        Optional<Course> courseOpt = Optional.ofNullable(courseDao.findById(registerDto.getCourseId()));
        if (courseOpt.isEmpty()) {
            model.addAttribute("courseError", "Invalid course ID");
            model.addAttribute("courses", courseDao.findAll());
            return "register";
        }

        Course course = courseOpt.get();

        studentDao.findByEmail(registerDto.getEmail()).ifPresentOrElse(student -> {
            student.setName(registerDto.getName());
            student.setDateOfBirth(registerDto.getDateOfBirth());
            if (!student.getCourses().contains(course)) {
                student.getCourses().add(course);
            }
            studentDao.update(student);
        }, () -> {
            Student student = Student.builder()
                    .email(registerDto.getEmail())
                    .name(registerDto.getName())
                    .dateOfBirth(registerDto.getDateOfBirth())
                    .courses(new ArrayList<>(List.of(course)))
                    .build();
            studentDao.save(student);
        });

        return "register_success";
    }

    @GetMapping("/students")
    public String studentPage(Model model) {
        model.addAttribute("students", studentDao.findAll());
        return "students";
    }

    @GetMapping("/courses")
    public String coursePage(Model model) {
        model.addAttribute("courses", courseDao.findAll());
        return "courses";
    }

    @PostMapping("/courses/add")
    public ResponseEntity<Void> addCourse(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("duration") String duration
    ) {
        if (title == null || title.isBlank() ||
                description == null || description.isBlank() ||
                duration == null || duration.isBlank()) {
            throw new IllegalArgumentException("All fields and the image file are required");
        }

        Course course = Course.builder()
                .title(title)
                .description(description)
                .duration(duration)
                .build();

        courseDao.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}


//<%--    input stud info    create stud if not exsists      by email identify      name, email, course validation   --%>
//<a href="${pageContext.request.contextPath}/register">Register</a>
//
//<%--    all courses      find by name --%>
//<a href="${pageContext.request.contextPath}/courses">Courses</a>
//
//<%--    all students      find by name --%>
//<a href="${pageContext.request.contextPath}/students">Students</a>
//
//<%--    see all courses all enroled students--%>
//<%--    <a href="${pageContext.request.contextPath}/students/enrollments">Add Courses</a>--%>
//
//<%--    add course --%>
//<%--    <a href="${pageContext.request.contextPath}/courses/add">Add Courses</a>--%>
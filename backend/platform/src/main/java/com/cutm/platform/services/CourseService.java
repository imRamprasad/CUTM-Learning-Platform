package com.cutm.platform.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cutm.platform.models.Course;
import com.cutm.platform.repositories.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Page<Course> getPublishedCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findByIsPublishedTrue(pageable);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course createCourse(Course course, String userId) {
        LocalDateTime now = LocalDateTime.now();
        course.setId(null);
        course.setCreatedBy(userId);
        course.setCreatedAt(now);
        course.setUpdatedAt(now);

        if (course.getIsPublished() == null) {
            course.setIsPublished(false);
        }
        if (course.getStatus() == null) {
            course.setStatus(course.getIsPublished() ? "PUBLISHED" : "DRAFT");
        }
        if (course.getEnrolledCount() == null) {
            course.setEnrolledCount(0);
        }
        if (course.getCompletedCount() == null) {
            course.setCompletedCount(0);
        }
        if (course.getAverageRating() == null) {
            course.setAverageRating(0.0);
        }

        return courseRepository.save(course);
    }

    public Course updateCourse(String id, Course incoming) {
        Course existing = getCourseById(id);

        existing.setTitle(incoming.getTitle());
        existing.setSlug(incoming.getSlug());
        existing.setDescription(incoming.getDescription());
        existing.setThumbnail(incoming.getThumbnail());
        existing.setLevel(incoming.getLevel());
        existing.setTopics(incoming.getTopics());
        existing.setModules(incoming.getModules());
        existing.setPrerequisites(incoming.getPrerequisites());
        existing.setStatus(incoming.getStatus());
        existing.setIsPublished(incoming.getIsPublished());
        existing.setUpdatedAt(LocalDateTime.now());

        return courseRepository.save(existing);
    }

    public void deleteCourse(String id) {
        courseRepository.deleteById(id);
    }

    public Course publishCourse(String id) {
        Course course = getCourseById(id);
        course.setIsPublished(true);
        course.setStatus("PUBLISHED");
        course.setUpdatedAt(LocalDateTime.now());
        return courseRepository.save(course);
    }
}

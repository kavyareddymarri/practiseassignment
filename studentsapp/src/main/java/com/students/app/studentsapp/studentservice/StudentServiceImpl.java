package com.students.app.studentsapp.studentservice;


import com.students.app.studentsapp.exception.ResourceNotFoundException;
import com.students.app.studentsapp.proxy.StudentFeign;
import com.students.app.studentsapp.studententity.StudentEntity;
import com.students.app.studentsapp.studentrepo.StudentRepo;
import com.students.app.studentsapp.studentresponse.CourseResponse;
import com.students.app.studentsapp.studentresponse.StudentResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {


    private StudentRepo studentRepo;

    private StudentFeign studentFeign;

    private ModelMapper modelMapper;
    @Autowired
    public StudentServiceImpl(StudentRepo studentRepo, StudentFeign studentFeign, ModelMapper modelMapper) {
        this.studentRepo = studentRepo;
        this.studentFeign = studentFeign;
        this.modelMapper = modelMapper;
    }

    @Override
    public StudentResponse getstudentsbyid(long id) {
      StudentEntity studentEntity =  studentRepo.findById(id).get();
      StudentResponse studentResponse =  modelMapper.map(studentEntity,StudentResponse.class);
        List<CourseResponse> courseResponse = studentFeign.getcoursesbyid(id);
        courseResponse.forEach(c-> studentResponse.setCourseResponse(c));
        return  studentResponse;
    }

    @Override
    public List<StudentResponse> getallstudents() {
        List<StudentEntity> studentEntities = studentRepo.findAll();
        List<StudentResponse> studentResponse = Arrays.asList(modelMapper.map(studentEntities,StudentResponse[].class));
        List<CourseResponse> courseResponseList = studentFeign.getAllcourses().getBody();

            studentResponse.forEach(student->{

                for (CourseResponse response: courseResponseList) {
                    if(student.getId()==response.getStudent_id())
                        student.setCourseResponse(response);}
            });
            return studentResponse;
    }

    @Override
    public void poststudents(StudentResponse studentResponse) {

        StudentEntity studentEntity = modelMapper.map(studentResponse,StudentEntity.class);
        studentRepo.save(studentEntity);
        Long student_id = studentEntity.getId();

        studentFeign.postCourses(student_id,studentResponse.getCourseResponse());
    }

    @Override
    public void updatestudents(Long id, StudentResponse studentResponse)  {
        StudentEntity studentEntity = studentRepo.findById(id).orElseThrow
                (()->new ResourceNotFoundException("Student doesnt exists with id"+id));
        studentFeign.putCourses(id,studentResponse.getCourseResponse());
        studentEntity.setEmail(studentResponse.getEmail());
        studentEntity.setMobilenumber(studentResponse.getMobilenumber());
        studentEntity.setName(studentResponse.getName());
        studentRepo.save(studentEntity);
    }

    @Override
    public void deletestudents(Long id) {
        StudentEntity studentEntity =  studentRepo.findById(id).orElseThrow
                (()->new ResourceNotFoundException("Student doesnt exists with id"+id));
        studentRepo.delete(studentEntity);
        studentFeign.deleteCourses(id);
    }
}

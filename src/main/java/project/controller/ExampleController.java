package project.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.Getter;
import lombok.Setter;

@Controller
public class ExampleController {
    
    @GetMapping("thymeleaf/example")
    public String thymeleafExample(Model model){
        Person examplePerson = new Person();
        examplePerson.setId(1L);
        examplePerson.setName("홍길동");
        examplePerson.setAge(11);
        List<String> list = Arrays.asList("운동", "독서");
        examplePerson.setHobbies(list);
        
        model.addAttribute("person", examplePerson);
        model.addAttribute("today", LocalDate.now());
        
        return "example";
    }
    
    @Setter
    @Getter
    class Person{
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }
}

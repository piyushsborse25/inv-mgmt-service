package com.cmp.inv.mgmt.service.engine;

import com.cmp.inv.mgmt.service.InvMgmtServiceApplication;
import com.cmp.inv.mgmt.service.entities.Customer;
import com.cmp.inv.mgmt.service.repos.CustomerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class Executor {

    public static void main(String[] args) {

        ConfigurableApplicationContext context =
                SpringApplication.run(InvMgmtServiceApplication.class, args);

        CustomerRepository myService = context.getBean(CustomerRepository.class);

        Specification<Customer> spec = (root, query, cb) -> cb.like(root.get("email"), "%gmail.com");
        List<Customer> result = myService.findAll(spec);
        result.forEach(System.out::println);

        context.close();
    }
}

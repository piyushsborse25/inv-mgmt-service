package com.cmp.inv.mgmt.service.engine;

import com.cmp.inv.mgmt.service.InvMgmtServiceApplication;
import com.cmp.inv.mgmt.service.enums.SearchField;
import com.cmp.inv.mgmt.service.registry.impl.FieldMetadataRegistry;
import com.cmp.inv.mgmt.service.repos.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class Executor {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Executor.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(InvMgmtServiceApplication.class, args);
        CustomerRepository myService = context.getBean(CustomerRepository.class);

        try {


            FieldMetadataRegistry registry = new FieldMetadataRegistry();

            System.out.println("Metadata: "+ registry.get(SearchField.CUSTOMER_FULL_NAME));



        } catch (Exception e) {
            System.out.println(e);
        } finally {
            context.close();
        }
    }
}

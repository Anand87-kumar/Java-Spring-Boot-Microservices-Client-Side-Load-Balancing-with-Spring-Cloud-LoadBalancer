package com.anand.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.anand.entity.Employee;
import com.anand.repository.EmployeeRepo;
import com.anand.response.AddressResponse;
import com.anand.response.EmployeeResponse;

import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    public EmployeeResponse getEmployeeById(int id) {
        Optional<Employee> employee = employeeRepo.findById(id);
        EmployeeResponse employeeResponse = mapper.map(employee, EmployeeResponse.class);

        ServiceInstance serviceInstance = loadBalancerClient.choose("ADDRESS-SERVICE");

        if (serviceInstance != null) {
            String uri = serviceInstance.getUri().toString();

            String contextPath = serviceInstance.getMetadata().get("configPath");

            AddressResponse addressResponse = restTemplate.getForObject(uri + contextPath + "/address/{id}", AddressResponse.class, id);

            employeeResponse.setAddressResponse(addressResponse);
        } else {
           
            employeeResponse.setAddressResponse(new AddressResponse());
        }

        return employeeResponse;
    }
}



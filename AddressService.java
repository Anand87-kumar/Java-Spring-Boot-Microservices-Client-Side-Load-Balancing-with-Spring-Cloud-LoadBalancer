package com.anand.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anand.repository.AddressRepo;
import com.anand.response.AddressResponse;
import com.anand.entity.Address;

import java.util.Optional;

@Service
public class AddressService {

	@Autowired
	private AddressRepo addressRepo;

	@Autowired
	private ModelMapper mapper;

	public AddressResponse findAddressByEmployeeId(int employeeId) {
		Optional<Address> addressByEmployeeId = addressRepo.findAddressByEmployeeId(employeeId);
		AddressResponse addressResponse = mapper.map(addressByEmployeeId, AddressResponse.class);
		return addressResponse;
	}

}

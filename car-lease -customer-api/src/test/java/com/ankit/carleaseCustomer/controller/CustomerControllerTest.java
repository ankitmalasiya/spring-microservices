package com.ankit.carleaseCustomer.controller;

import com.ankit.carleaseCustomer.entity.Customer;
import com.ankit.carleaseCustomer.repository.CustomerRepository;
import com.ankit.carleaseCustomer.service.ICustomerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class CustomerControllerTest {

    ObjectMapper om = new ObjectMapper();
    @Autowired
    ICustomerService customerService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    MockMvc mockMvc;

    Map<String, Customer> testData;

    @Before
    public void setup() {
        customerRepository.deleteAll();
        testData = getTestData();
    }

    @Test
    public void testCustomerCreationWithValidData() throws Exception {
        Customer expectedRecord = testData.get("1");
        assertEquals("Ankit", expectedRecord.getName());

        Customer actualRecord = om.readValue(mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Customer.class);

        Assert.assertTrue(new ReflectionEquals(expectedRecord, "id").matches(actualRecord));
        assertTrue(customerService.getCustomerById(actualRecord.getId()).isPresent());

        expectedRecord = testData.get("2");
        assertEquals("Aks", expectedRecord.getName());

        actualRecord = om.readValue(mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Customer.class);

        Assert.assertTrue(new ReflectionEquals(expectedRecord, "id").matches(actualRecord));
        assertTrue(customerService.getCustomerById(actualRecord.getId()).isPresent());

        expectedRecord.setName("Ankit");
        mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetCustomers() throws Exception {
        Map<String, Customer> testData = getTestData();

        Map<String, Customer> expectedMap = new HashMap<>();
        List<Customer> expected = new ArrayList<>();
        for (Map.Entry<String, Customer> kv : testData.entrySet()) {
            Customer response = om.readValue(mockMvc.perform(post("/customers")
                    .contentType("application/json")
                    .content(om.writeValueAsString(kv.getValue())))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Customer.class);
            expectedMap.put(kv.getKey(), response);
        }
        Arrays.sort(expectedMap.values().toArray(new Customer[testData.size()]), Comparator.comparing(Customer::getId));

        List<Customer> actualRecords = om.readValue(mockMvc.perform(get("/customers"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<Customer>>() {
        });
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertTrue(new ReflectionEquals(expected.get(i), "id").matches(actualRecords.get(i)));
        }

        actualRecords = om.readValue(mockMvc.perform(get("/customers?type=buy"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<Customer>>() {
        });
        Assert.assertTrue(new ReflectionEquals(expectedMap.get("1"), "id").matches(actualRecords.get(0)));

        expected = expectedMap.entrySet().stream().filter(kv -> kv.equals("1")).map(Map.Entry::getValue).collect(Collectors.toList());
        expected.sort(Comparator.comparing(Customer::getId));

        actualRecords = om.readValue(mockMvc.perform(get("/customers?type=sell"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<Customer>>() {
        });
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertTrue(new ReflectionEquals(expected.get(i), "id").matches(actualRecords.get(i)));
        }

        mockMvc.perform(get("/customers"))
                .andDo(print())
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(status().isOk());

        expected = Arrays.asList(expectedMap.entrySet().stream().filter(kv -> "1,user23_sell_AAC".contains(kv.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).values().toArray(new Customer[2]));
        expected.sort(Comparator.comparing(Customer::getId));

        actualRecords = om.readValue(mockMvc.perform(get("/customers?user_id=23"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<Customer>>() {
        });
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertTrue(new ReflectionEquals(expected.get(i), "id").matches(actualRecords.get(i)));
        }
    }

    @Test
    public void testGetCustomerRecordWithId() throws Exception {
        Customer expectedRecord = getTestData().get("1");
        expectedRecord = om.readValue(mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Customer.class);

        //for existing
        Customer actualRecord = om.readValue(mockMvc.perform(get("/customers/" + expectedRecord.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), Customer.class);

        Assert.assertTrue(new ReflectionEquals(expectedRecord).matches(actualRecord));

        //non existing
        mockMvc.perform(get("/customers/" + Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    private Map<String, Customer> getTestData() {
        Map<String, Customer> data = new HashMap<>();

        Customer customer1 = Customer.builder()
                .name("Ankit")
                .street("2e Delistraat")
                .houseNumber("31")
                .zipCode("3531KR")
                .place("Utrecht")
                .build();
        data.put("1", customer1);

        Customer customer2 = Customer.builder()
                .name("Aks")
                .street("Spechtenkamp")
                .houseNumber("266")
                .zipCode("3607KR")
                .place("Maarssen")
                .build();
        data.put("2", customer2);

        return data;
    }
}
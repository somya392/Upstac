package org.upgrad.upstac.testrequests.consultation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;
import static org.upgrad.upstac.exception.UpgradResponseStatusException.asConstraintViolation;


@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    Logger log = LoggerFactory.getLogger(ConsultationController.class);




    @Autowired
    private TestRequestUpdateService testRequestUpdateService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;


    @Autowired
    TestRequestFlowService  testRequestFlowService;

    @Autowired
    private UserLoggedInService userLoggedInService;



    @GetMapping("/in-queue")
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForConsultations()  {

        /* Now we will use findBy() method of TestRequestQueryService
        The method will be accessed via existing object testRequestQueryService
        findBy is an already implemented method which takes an Enum value as parameter
        and returns list of all test requests depending upon the enum value provided as parameter*/
        return testRequestQueryService.findBy(RequestStatus.LAB_TEST_COMPLETED);


    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForDoctor()  {

        //Get current logged in user
        User userName = userLoggedInService.getLoggedInUser();

        /* Now we will use findByDoctor() method of TestRequestQueryService
        The method will be accessed via existing object testRequestQueryService
        findByDoctor is an already implemented method which takes object of User class as parameter
        and returns list of all test requests assigned to current doctor */
        return testRequestQueryService.findByDoctor(userName);




    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForConsultation(@PathVariable Long id) {

        // Implement this method

        // Implement this method to assign a particular test request to the current doctor(logged in user)
        //Create an object of User class and get the current logged in user
        //Create an object of TestRequest class and use the assignForConsultation() method of testRequestUpdateService to assign the particular id to the current user
        // return the above created object
        // For reference check the method assignForLabTest() method from LabRequestController class
        try {

            //Get current logged in user
            User userName = userLoggedInService.getLoggedInUser();

            /* Now we will use assignForConsultation() method of TestRequestUpdateService
            The method will be accessed via existing object testRequestUpdateService
            assignForConsultation is an already implemented method which takes patient id and object of User class as parameter
            and assigns the patient to current logged in doctor */
            return testRequestUpdateService.assignForConsultation(id,userName);

        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/update/{id}")
    public TestRequest updateConsultation(@PathVariable Long id,@RequestBody CreateConsultationRequest testResult) {

        // Implement this method

        // Implement this method to update the result of the current test request id with test doctor comments
        // Create an object of the User class to get the logged in user
        // Create an object of TestRequest class and make use of updateConsultation() method from testRequestUpdateService class
        //to update the current test request id with the testResult details by the current user(object created)
        // For reference check the method updateLabTest() method from LabRequestController class

        try {
            //Get current logged in user
            User userName = userLoggedInService.getLoggedInUser();

            /* Now we will use updateConsultation() method of TestRequestUpdateService
            The method will be accessed via existing object testRequestUpdateService
            updateConsultation is an already implemented method which takes patient id, test result and object of User class as parameter
            and updates consultation details of the patient*/
            return testRequestUpdateService.updateConsultation(id,testResult,userName);


        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



}

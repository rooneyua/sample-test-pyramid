# Sample Test Pyramid

This repository is to demonstrate a TestPyramid implementation, with different automated test layers in action. For the purpose of this demonstration, we have built a web application (backed by a http service) which is the application under testing. 

## What is TestPyramid?

[Learn from Martin Fowler](https://martinfowler.com/bliki/TestPyramid.html)

## Application under test

The application we have is a scaled-down version of shopping-cart which has sign-in and coupon-validation features.

The application itself has two functional components,
* Web application layer or simply the UI layer
* Service Layer

## Test scenarios

Following is a list of test scenarios related to Login feature,

* Verify successful login
* Verify not providing any input and submitting the form, results in appropriate error message
* Verify on providing partial input say email-id or password and submitting the form, results in appropriate error message
* Verify on providing invalid email-id, results in appropriate error message
* Verify on providing an email-id which does not correspond to a registered user and submitting the form, results in appropriate error message
* Verify on providing an email-id of a disabled user and submitting the form, results in appropriate error message
* Verify on providing an email-id of user whos account is not verified, results in appropriate error message
* Verify on attempting login with an email-id and wrong password for 3 times, result in user account getting locked

package com.example;

import org.junit.jupiter.api.Test;

class HelloWorldTest {

    @Test
    void testHello() {
        //Arrange
        HelloWorld helloWorld = new HelloWorld();

        //Act
        var result = helloWorld.hello();

        //Assert
        assert helloWorld.hello() == true;
    }

}
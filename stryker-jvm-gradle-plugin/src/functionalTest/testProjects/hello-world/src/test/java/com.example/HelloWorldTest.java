package com.example;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelloWorldTest {

    @Test
    void testHello() {
        //Arrange
        HelloWorld helloWorld = new HelloWorld();

        //Act
        var result = helloWorld.hello();

        //Assert
        assertTrue(result);
    }

    @Nested
    class NestedHelloWorldTest {
        @Test
        void testNestedWorld() {
            //Arrange
            HelloWorld helloWorld = new HelloWorld();

            //Act
            var result = helloWorld.world();

            //Assert
            assertFalse(result);
        }
    }

}
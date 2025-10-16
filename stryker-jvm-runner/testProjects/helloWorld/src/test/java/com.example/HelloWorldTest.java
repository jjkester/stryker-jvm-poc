package com.example;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HelloWorldTest {

    @Test
    void testHello() {
        //Arrange
        HelloWorld helloWorld = new HelloWorld();

        //Act
        var result = helloWorld.hello();

        //Assert
        assert result == true;
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
            assert result == false;
        }
    }

}
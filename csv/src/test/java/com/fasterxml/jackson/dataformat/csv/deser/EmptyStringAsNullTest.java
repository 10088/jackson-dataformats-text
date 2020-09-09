package com.fasterxml.jackson.dataformat.csv.deser;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.fasterxml.jackson.databind.ObjectReader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.ModuleTestBase;

/**
 * Tests for {@link CsvParser.Feature#EMPTY_STRING_AS_NULL}
 * ({@code dataformats-text#7}).
 */
public class EmptyStringAsNullTest
    extends ModuleTestBase
{
    @JsonPropertyOrder({"firstName", "middleName", "lastName"})
    static class TestUser {
        public String firstName, middleName, lastName;
    }

    /*
    /**********************************************************
    /* Test methods
    /**********************************************************
     */

    private final CsvMapper MAPPER = mapperForCsv();

    public void testEmptyStringAsNullDisabled() throws Exception {
        // setup test data
        TestUser expectedTestUser = new TestUser();
        expectedTestUser.firstName = "Grace";
        expectedTestUser.middleName = "";
        expectedTestUser.lastName = "Hopper";
        ObjectReader objectReader = MAPPER.readerFor(TestUser.class).with(MAPPER.schemaFor(TestUser.class));
        String csv = "Grace,,Hopper";

        // execute
        TestUser actualTestUser = objectReader.readValue(csv);

        // test
        assertNotNull(actualTestUser);
        assertEquals(expectedTestUser.firstName, actualTestUser.firstName);
        assertEquals(expectedTestUser.middleName, actualTestUser.middleName);
        assertEquals(expectedTestUser.lastName, actualTestUser.lastName);
    }

    public void testEmptyStringAsNullEnabled() throws Exception {
        // setup test data
        TestUser expectedTestUser = new TestUser();
        expectedTestUser.firstName = "Grace";
        expectedTestUser.lastName = "Hopper";

        ObjectReader objectReader = MAPPER
                .readerFor(TestUser.class)
                .with(MAPPER.schemaFor(TestUser.class))
                .with(CsvParser.Feature.EMPTY_STRING_AS_NULL);
        String csv = "Grace,,Hopper";

        // execute
        TestUser actualTestUser = objectReader.readValue(csv);

        // test
        assertNotNull(actualTestUser);
        assertEquals(expectedTestUser.firstName, actualTestUser.firstName);
        assertNull("The column that contains an empty String should be deserialized as null ", actualTestUser.middleName);
        assertEquals(expectedTestUser.lastName, actualTestUser.lastName);
    }
}

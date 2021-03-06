package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.AddressBook;
import seedu.address.storage.XmlAdaptedGroup;
import seedu.address.storage.XmlAdaptedPerson;
import seedu.address.storage.XmlAdaptedTag;
import seedu.address.storage.XmlAdaptedTest;
import seedu.address.storage.XmlSerializableAddressBook;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TestUtil;

public class XmlUtilTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlUtilTest");
    private static final Path EMPTY_FILE = TEST_DATA_FOLDER.resolve("empty.xml");
    private static final Path MISSING_FILE = TEST_DATA_FOLDER.resolve("missing.xml");
    private static final Path VALID_FILE = TEST_DATA_FOLDER.resolve("validAddressBook.xml");
    private static final Path MISSING_PERSON_FIELD_FILE = TEST_DATA_FOLDER.resolve("missingPersonField.xml");
    private static final Path MISSING_GROUP_FIELD_FILE = TEST_DATA_FOLDER.resolve("missingGroupField.xml");
    private static final Path INVALID_PERSON_FIELD_FILE = TEST_DATA_FOLDER.resolve("invalidPersonField.xml");
    private static final Path INVALID_GROUP_FIELD_FILE = TEST_DATA_FOLDER.resolve("invalidGroupField.xml");
    private static final Path VALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("validPerson.xml");
    private static final Path VALID_GROUP_FILE = TEST_DATA_FOLDER.resolve("validGroup.xml");
    private static final Path TEMP_FILE = TestUtil.getFilePathInSandboxFolder("tempAddressBook.xml");

    private static final String INVALID_PHONE = "9482asf424";
    private static final String INVALID_GROUP_LOCATION = "E1-01-01*";

    private static final String VALID_NAME = "Hans Muster";
    private static final String VALID_GENDER = "MALE";
    private static final String VALID_NATIONALITY = "UK";
    private static final String VALID_PHONE = "9482424";
    private static final String VALID_EMAIL = "hans@example";
    private static final String VALID_ADDRESS = "4th street";
    private static final String VALID_GRADE = "100";
    private static final String VALID_GROUP_NAME = "TUT[1]";
    private static final String VALID_GROUP_LOCATION = "E1-01-01";
    private static final List<XmlAdaptedTag> VALID_TAGS = Collections.singletonList(new XmlAdaptedTag("friends"));
    private static final List<XmlAdaptedTag> VALID_GROUP_TAGS = Collections.singletonList(new XmlAdaptedTag("maths"));
    private static final List<XmlAdaptedPerson> VALID_PERSONS = Collections.singletonList(new XmlAdaptedPerson(ALICE));
    private static final List<XmlAdaptedTest> VALID_TEST =
            Collections.singletonList(new XmlAdaptedTest("CS2113quiz", "77" , "A"));
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, AddressBook.class);
    }

    @Test
    public void getDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, AddressBook.class);
    }

    @Test
    public void getDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, AddressBook.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        AddressBook dataFromFile = XmlUtil.getDataFromFile(VALID_FILE, XmlSerializableAddressBook.class).toModelType();
        assertEquals(9, dataFromFile.getPersonList().size());
        assertEquals(5, dataFromFile.getGroupList().size());
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithMissingPersonField_validResult() throws Exception {
        XmlAdaptedPerson actualPerson = XmlUtil.getDataFromFile(
                MISSING_PERSON_FIELD_FILE, XmlAdaptedPersonWithRootElement.class);
        XmlAdaptedPerson expectedPerson = new XmlAdaptedPerson(
                null, VALID_GENDER, VALID_NATIONALITY, VALID_PHONE,
                VALID_EMAIL, VALID_ADDRESS, VALID_TAGS, VALID_TEST);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithInvalidPersonField_validResult() throws Exception {
        XmlAdaptedPerson actualPerson = XmlUtil.getDataFromFile(
                INVALID_PERSON_FIELD_FILE, XmlAdaptedPersonWithRootElement.class);
        XmlAdaptedPerson expectedPerson = new XmlAdaptedPerson(
                VALID_NAME, VALID_GENDER, VALID_NATIONALITY, INVALID_PHONE,
                VALID_EMAIL, VALID_ADDRESS, VALID_TAGS, VALID_TEST);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithValidPerson_validResult() throws Exception {
        XmlAdaptedPerson actualPerson = XmlUtil.getDataFromFile(
                VALID_PERSON_FILE, XmlAdaptedPersonWithRootElement.class);
        XmlAdaptedPerson expectedPerson = new XmlAdaptedPerson(
                VALID_NAME, VALID_GENDER, VALID_NATIONALITY, VALID_PHONE,
                VALID_EMAIL, VALID_ADDRESS, VALID_TAGS, VALID_TEST);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void xmlAdaptedGroupFromFile_fileWithMissingGroupField_validResult() throws Exception {
        XmlAdaptedGroup actualGroup = XmlUtil.getDataFromFile(
                MISSING_GROUP_FIELD_FILE, XmlAdaptedGroupWithRootElement.class);
        XmlAdaptedGroup expectedGroup = new XmlAdaptedGroup(
                null, VALID_GROUP_LOCATION, VALID_GROUP_TAGS, VALID_PERSONS);
        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    public void xmlAdaptedGroupFromFile_fileWithInvalidGroupField_validResult() throws Exception {
        XmlAdaptedGroup actualGroup = XmlUtil.getDataFromFile(
                INVALID_GROUP_FIELD_FILE, XmlAdaptedGroupWithRootElement.class);
        XmlAdaptedGroup expectedGroup = new XmlAdaptedGroup(
                VALID_GROUP_NAME, INVALID_GROUP_LOCATION, VALID_GROUP_TAGS, VALID_PERSONS);
        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    public void xmlAdaptedGroupFromFile_fileWithValidGroup_validResult() throws Exception {
        XmlAdaptedGroup actualGroup = XmlUtil.getDataFromFile(
                VALID_GROUP_FILE, XmlAdaptedGroupWithRootElement.class);
        XmlAdaptedGroup expectedGroup = new XmlAdaptedGroup(
                VALID_GROUP_NAME, VALID_GROUP_LOCATION, VALID_GROUP_TAGS, VALID_PERSONS);
        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new AddressBook());
    }

    @Test
    public void saveDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new AddressBook());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        FileUtil.createFile(TEMP_FILE);
        XmlSerializableAddressBook dataToWrite = new XmlSerializableAddressBook(new AddressBook());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableAddressBook.class);
        assertEquals(dataToWrite, dataFromFile);

        AddressBookBuilder builder = new AddressBookBuilder(new AddressBook());
        dataToWrite = new XmlSerializableAddressBook(
                builder.withPerson(new PersonBuilder().build()).build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableAddressBook.class);
        assertEquals(dataToWrite, dataFromFile);
    }

    /**
     * Test class annotated with {@code XmlRootElement} to allow unmarshalling of .xml data to {@code XmlAdaptedPerson}
     * objects.
     */
    @XmlRootElement(name = "person")
    private static class XmlAdaptedPersonWithRootElement extends XmlAdaptedPerson {}

    /**
     * Test class annotated with {@code XmlRootElement} to allow unmarshalling of .xml data to {@code XmlAdaptedGroup}
     * objects.
     */
    @XmlRootElement(name = "groups")
    private static class XmlAdaptedGroupWithRootElement extends XmlAdaptedGroup {}
}

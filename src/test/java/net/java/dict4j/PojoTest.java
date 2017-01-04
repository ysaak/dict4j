package net.java.dict4j;

import org.junit.Test;

import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

public class PojoTest {
    // The package to test
    private static final String POJO_PACKAGE = "net.java.dict4j.data";

    @Test
    public void testPojo() {
        Validator validator = ValidatorBuilder.create()
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRule())
                .with(new GetterTester())
                .with(new SetterTester())
                .build();

        validator.validate(POJO_PACKAGE, new FilterPackageInfo());
    }
}

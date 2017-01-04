package net.java.dict4j;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import com.openpojo.validation.utils.ValidationHelper;

public class PojoTest {
    // The package to test
    private static final String POJO_PACKAGE = "net.java.dict4j.data";

    @Test
    public void testGettersSetters() {
        Validator validator = ValidatorBuilder.create()
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRule())
                .with(new GetterTester())
                .with(new SetterTester())
                .build();

        validator.validate(POJO_PACKAGE, new FilterPackageInfo());
    }
    
    @Test
    public void testToString() throws Exception {
        Reflections reflections = new Reflections(POJO_PACKAGE, new SubTypesScanner(false));
        
        Set<Class<? extends Object>> classes = reflections.getSubTypesOf(Object.class);
        
        for (Class<?> o : classes) {
            
            PojoClass pojoClass = PojoClassFactory.getPojoClass(o);
            
            Object instance = ValidationHelper.getBasicInstance(pojoClass);
            
            Assert.assertNotNull("null toString for class '" + o.getName() + "'", instance.toString());
            Assert.assertTrue("toString does not start with class name for class '" + o.getName() + "'", instance.toString().startsWith(o.getSimpleName()));
        }
    }
}

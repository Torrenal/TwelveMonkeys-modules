package com.twelvemonkeys.util.convert;

import com.twelvemonkeys.lang.Validate;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URI;

import static org.junit.Assert.*;

/**
 * DefaultConverterTestCase
 * <p/>
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haku $
 * @version $Id: //depot/branches/personal/haraldk/twelvemonkeys/release-2/twelvemonkeys-core/src/test/java/com/twelvemonkeys/util/convert/DefaultConverterTestCase.java#1 $
 */
public class DefaultConverterTestCase extends PropertyConverterAbstractTestCase {
    protected PropertyConverter makePropertyConverter() {
        return new DefaultConverter();
    }

    protected Conversion[] getTestConversions() {
        //noinspection BooleanConstructorCall
        return new Conversion[] {
                // Booleans
                new Conversion("true", Boolean.TRUE),
                new Conversion("TRUE", Boolean.TRUE, null, "true"),
                new Conversion("false", Boolean.FALSE),
                new Conversion("FALSE", false, null, "false"),

                new Conversion("2", 2),

                // Stupid but valid
                new Conversion("fooBar", "fooBar"),
                //new Conversion("fooBar", new StringBuilder("fooBar")), - StringBuilder does not impl equals()...

                // Stupid test class that reveres chars
                new Conversion("fooBar", new FooBar("fooBar")),

                // String array tests
                new Conversion("foo, bar, baz", new String[] {"foo", "bar", "baz"}),
                new Conversion("foo", new String[] {"foo"}),
                new Conversion("foo;bar; baz", new String[] {"foo", "bar", "baz"}, "; ", "foo; bar; baz"),

                // Native array tests
                new Conversion("1, 2, 3", new int[] {1, 2, 3}),
                new Conversion("-1, 42, 0", new long[] {-1, 42, 0}),
                new Conversion("true, true, false", new boolean[] {true, true, false}),
                new Conversion(".3, 4E7, .97", new float[] {.3f, 4e7f, .97f}, ", ", "0.3, 4.0E7, 0.97"),

                // Object array test
                new Conversion("foo, bar", new FooBar[] {new FooBar("foo"), new FooBar("bar")}),
                new Conversion("/temp, /usr/local/bin".replace('/', File.separatorChar), new File[] {new File("/temp"), new File("/usr/local/bin")}),
                new Conversion("file:/temp, http://java.net/", new URI[] {URI.create("file:/temp"), URI.create("http://java.net/")}),

                // TODO: More tests
        };
    }

    @Test
    public void testConvertBooleanPrimitive() {
        PropertyConverter converter = makePropertyConverter();
        assertTrue((Boolean) converter.toObject("true", boolean.class, null));
        assertFalse((Boolean) converter.toObject("FalsE", Boolean.TYPE, null));
    }

    @Test
    public void testConvertShortPrimitive() {
        PropertyConverter converter = makePropertyConverter();
        assertEquals(1, (short) (Short) converter.toObject("1", short.class, null));
        assertEquals(-2, (short) (Short) converter.toObject("-2", Short.TYPE, null));
    }
    @Test
    public void testConvertIntPrimitive() {
        PropertyConverter converter = makePropertyConverter();
        assertEquals(1, (int) (Integer) converter.toObject("1", int.class, null));
        assertEquals(-2, (int) (Integer) converter.toObject("-2", Integer.TYPE, null));
    }

    @Test
    public void testConvertLongPrimitive() {
        PropertyConverter converter = makePropertyConverter();
        assertEquals(Long.MAX_VALUE, (long) (Long) converter.toObject("9223372036854775807", long.class, null));
        assertEquals(-2, (long) (Long) converter.toObject("-2", Long.TYPE, null));
    }

    @Test
    public void testConvertBytePrimitive() {
        PropertyConverter converter = makePropertyConverter();
        assertEquals(1, (byte) (Byte) converter.toObject("1", byte.class, null));
        assertEquals(-2, (byte) (Byte) converter.toObject("-2", Byte.TYPE, null));
    }

    @Test
    public void testConvertFloatPrimitive() {
        PropertyConverter converter = makePropertyConverter();
        assertEquals(1f, (Float) converter.toObject("1.0", float.class, null), 0);
        assertEquals(-2.3456f, (Float) converter.toObject("-2.3456", Float.TYPE, null), 0);
    }

    @Test
    public void testConvertDoublePrimitive() {
        PropertyConverter converter = makePropertyConverter();
        assertEquals(1d, (Double) converter.toObject("1.0", double.class, null), 0);
        assertEquals(-2.3456, (Double) converter.toObject("-2.3456", Double.TYPE, null), 0);
    }

    @Ignore("Known issue. Why would anyone do something like this?")
    @Test
    public void testConvertCharPrimitive() {
        PropertyConverter converter = makePropertyConverter();
        assertEquals('A', (char) (Character) converter.toObject("A", char.class, null));
        assertEquals('Z', (char) (Character) converter.toObject("Z", Character.TYPE, null));
    }

    public static class FooBar {
        private final String bar;

        public FooBar(String pFoo) {
            Validate.notNull(pFoo, "foo");

            bar = reverse(pFoo);
        }

        private String reverse(String pFoo) {
            StringBuilder buffer = new StringBuilder(pFoo.length());

            for (int i = pFoo.length() - 1; i >= 0; i--) {
                buffer.append(pFoo.charAt(i));
            }

            return buffer.toString();
        }

        public String toString() {
            return reverse(bar);
        }

        public boolean equals(Object obj) {
            return obj == this || (obj != null && obj.getClass() == getClass() && ((FooBar) obj).bar.equals(bar));
        }

        public int hashCode() {
            return 7 * bar.hashCode();
        }
    }
}

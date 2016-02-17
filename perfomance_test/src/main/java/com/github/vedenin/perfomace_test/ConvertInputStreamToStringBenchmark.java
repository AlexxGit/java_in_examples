package com.github.vedenin.perfomace_test;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.apache.commons.io.IOUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by vvedenin on 2/15/2016.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class ConvertInputStreamToStringBenchmark {
    private final static String test1 = "test1847686128764812764876128пввпв76417826487216478216784621784672816478216784621784621786478216478216784261784621"+ "\n\r" +"782178647281647821647821697421687126784621874621786478216478216874";
    private final static InputStream inputStream = IOUtils.toInputStream(test1, StandardCharsets.UTF_8);

    /*             1. Using IOUtils.toString (Apache Utils) */
    @Benchmark
    public String apacheToInputStream() throws IOException {
        mark();
        String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        reset();
        return result;
    }

    /*             2. Using CharStreams (guava) */
    @Benchmark
    public String guavaCharStreams() throws IOException {
        mark();
        String result = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
        inputStream.reset();
        return result;
    }

    /*             3. Using Scanner (JDK) */
    @Benchmark
    public String jdkScanner() throws IOException {
        mark();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        reset();
        return result;
    }

    /*             4. Using Stream Api (Java 8) */
    @Benchmark
    public String jdkJava8() throws IOException {
        mark();
        String result = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        reset();
        return result;
    }

    /*             5. Using parallel Stream Api (Java 8) */
    @Benchmark
    public String jdkJava8parallel() throws IOException {
        mark();
        String result = new BufferedReader(new InputStreamReader(inputStream)).lines().parallel().collect(Collectors.joining("\n"));
        reset();
        return result;
    }

    /*             6. Using InputStreamReader and StringBuilder (JDK) */
    @Benchmark
    public String inputStreamReaderAndStringBuilder() throws IOException {
        mark();
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        reset();
        return out.toString();
    }

    /*             7. Using StringWriter and IOUtils.copy (Apache Commons)*/
    @Benchmark
    public String apacheStringWriterAndIOUtilsCopy() throws IOException {
        mark();
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        reset();
        return writer.toString();
    }

    /*            8. Using ByteArrayOutputStream and inputStream.read (JDK)  */
    @Benchmark
    public String readByteArrayOutputStream() throws IOException {
        mark();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        reset();
        return result.toString("UTF-8");
    }

    /*            9. Using BufferedReader (JDK) */
    @Benchmark
    public String bufferedReaderReadLine() throws IOException {
        mark();
        String newLine = System.getProperty("line.separator");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line; boolean flag = false;
        while ((line = reader.readLine()) != null) {
            result.append(flag? newLine: "").append(line);
            flag = true;
        }
        reset();
        return result.toString();
    }

    /*            10. Using BufferedInputStream and ByteArrayOutputStream (JDK) */
    @Benchmark
    public String bufferedInputStreamAndByteArrayOutputStream() throws IOException {
        mark();
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result != -1) {
            buf.write((byte) result);
            result = bis.read();
        }
        reset();
        return buf.toString();
    }

    /*            11. Using inputStream.read() and StringBuilder  (JDK) */
    @Benchmark
    public String inputStreamReadAndStringBuilder() throws IOException {
        mark();
        int ch;
        StringBuilder sb = new StringBuilder();
        while((ch = inputStream.read()) != -1)
            sb.append((char)ch);
        reset();
        return sb.toString();
    }

    private void reset() throws IOException {
        inputStream.reset();
    }

    private void mark() {
        inputStream.mark(0);
    }

    public static void main(String[] args) throws Exception {
        ConvertInputStreamToStringBenchmark test = new ConvertInputStreamToStringBenchmark();
        System.out.println();
        System.out.println("1. apacheToInputStream : " + test.apacheToInputStream());
        System.out.println("2. guavaCharStreams : " + test.guavaCharStreams());
        System.out.println("3. jdkScanner : " + test.jdkScanner());
        System.out.println("4. jdkJava8 : " + test.jdkJava8());
        System.out.println("5. jdkJava8parallel : " + test.jdkJava8parallel());
        System.out.println("6. inputStreamReaderAndStringBuilder : " + test.inputStreamReaderAndStringBuilder());
        System.out.println("7. apacheStringWriterAndIOUtilsCopy : " + test.apacheStringWriterAndIOUtilsCopy());
        System.out.println("8. readByteArrayOutputStream : " + test.readByteArrayOutputStream());
        System.out.println("9. bufferedReaderReadLine : " + test.bufferedReaderReadLine());
        System.out.println("10. bufferedInputStreamAndByteArrayOutputStream : " + test.bufferedInputStreamAndByteArrayOutputStream());
        System.out.println("11. inputStreamReadAndStringBuilder : " + test.inputStreamReadAndStringBuilder());


        System.out.println();

        Options opt = new OptionsBuilder()
                .include(ConvertInputStreamToStringBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

}

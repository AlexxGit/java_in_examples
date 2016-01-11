## java Collections and Stream Api in Examples 

- [I. Stream API](#i-stream-api)
- [1. All way to create Stream in Java 8](#1-all-way-to-create-stream-in-java-8)
  
#### I. Stream API

###### 1. All way to create Stream in Java 8

Way to create stream	 | 	Template	 | 	Example
-------------	 | 	-------------	 | 	-------------
1. Clasic: Create stream from collection	 | 	collection.stream()	 | 	 Collection<String> collection = Arrays.asList("a1", "a2", "a3");  <br/>     Stream<String> streamFromCollection = collection.stream();
2. Create stream from values	 | 	Stream.of(value1,… ,valueN)	 | 	Stream<String> streamFromValues = Stream.of("a1", "a2", "a3");
3. Create stream from array	 | 	Arrays.stream(array)	 | 	String[] array = {"a1","a2","a3"};   <br/>         Stream<String> streamFromArrays = Arrays.stream(array);        
3. Create stream from part of array	 | 	Arrays.stream(array, start, end)	 | 	String[] array = {"a1","a2","a3"};<br/>              Stream<String> streamFromArrays = Arrays.stream(array, 1, 2);        
4. Create stream from file (every row from file become element of stream)	 | 	Files.lines(file_path)	 | 	Stream<String> streamFromFiles = Files.lines(Paths.get("file.txt"));
5. Create stream from stirng (every char become element of stream)	 | 	"string".chars()	 | 	IntStream streamFromString = "123".chars();
6. Using Stream.builder	 | 	Stream.builder().add(...)....build()	 | 	Stream.builder().add("a1").add("a2").add("a3").build();
7. Create parallel stream from collection	 | 	collection.parallelStream()	 | 	Stream<String> stream = collection.parallelStream();
8. Create infinive strean using Stream.iterate	 | 	Stream.iterate(init_value, generate_expression)	 | 	Stream<Integer> streamFromIterate = Stream.iterate(1, n -> n + 1);
9. Create infinive strean using Stream.generate	 | 	Stream.generate(generate_expression)	 | 	Stream<String> streamFromGenerate = Stream.generate(() -> "a1");
10. Create stream from path	 | 	Files.list(file_path)	 | 	Stream<Path> streamFromPath = Files.list(Paths.get("C:\\"));
11. Create stream from finding files	 | 	Files.find(file_path, max_depth, mathcher)	 | 	…
11. Create stream from files tree	 | 	Files.walk(file_path)	 | 	…
12. Create stream from all entities of jar file	 | 	new JarFile(jar_file).stream()	 | 	…
13. Create stream from all entities of zip file	 | 	new ZipFile(zip_file).stream()	 | 	…
14. Create stream from iterator	 | final Iterable<T> iterable = () -> iterator; <br /> StreamSupport.stream(iterable.spliterator(), false);	 | 	...
15. Create stream from iterable	 | 	StreamSupport.stream(iterable.spliterator(), false) | 	…
16. Create infinive stream from iterator	 | 	Stream.generate(iterator::next) | 	…

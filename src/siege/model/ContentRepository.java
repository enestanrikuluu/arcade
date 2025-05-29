package siege.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Manages the educational content (questions and information)
 */
public class ContentRepository {
    private ArrayList<String> slQuestions;
    private ArrayList<String> taQuestions;
    private ArrayList<String> profQuestions;
    
    private ArrayList<String> slInfo;
    private ArrayList<String> taInfo;
    private ArrayList<String> profInfo;
    
    private SecureRandom random;
    
    /**
     * Creates a new content repository
     */
    public ContentRepository() {
        slQuestions = new ArrayList<>();
        taQuestions = new ArrayList<>();
        profQuestions = new ArrayList<>();
        
        slInfo = new ArrayList<>();
        taInfo = new ArrayList<>();
        profInfo = new ArrayList<>();
        
        random = new SecureRandom();
    }
    
    /**
     * Loads questions and information from files
     */
    public void loadContent(String questionsPath, String infoPath) throws GameException {
        try {
            loadQuestions(questionsPath);
            loadInfo(infoPath);
        } catch (IOException e) {
            throw new GameException("Failed to load content files: " + e.getMessage(), e);
        }
    }
    
    /**
     * Loads questions from file
     */
    private void loadQuestions(String filePath) throws IOException {
        File file = new File(filePath);
        
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("Created empty questions file: " + filePath);
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String currentSection = "";
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty()) {
                    continue;
                }
                
                if (line.startsWith("#")) {
                    currentSection = line.toLowerCase();
                    continue;
                }
                
                if (currentSection.contains("sl")) {
                    slQuestions.add(line);
                } else if (currentSection.contains("ta")) {
                    taQuestions.add(line);
                } else if (currentSection.contains("prof")) {
                    profQuestions.add(line);
                }
            }
        }
        
        // Add default questions if lists are empty
        if (slQuestions.isEmpty()) {
            addDefaultSLQuestions();
        }
        if (taQuestions.isEmpty()) {
            addDefaultTAQuestions();
        }
        if (profQuestions.isEmpty()) {
            addDefaultProfQuestions();
        }
    }
    
    /**
     * Loads information from file
     */
    private void loadInfo(String filePath) throws IOException {
        File file = new File(filePath);
        
        // Create empty file if it doesn't exist
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("Created empty info file: " + filePath);
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String currentSection = "";
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty()) {
                    continue;
                }
                
                if (line.startsWith("#")) {
                    currentSection = line.toLowerCase();
                    continue;
                }
                
                if (currentSection.contains("sl")) {
                    slInfo.add(line);
                } else if (currentSection.contains("ta")) {
                    taInfo.add(line);
                } else if (currentSection.contains("prof")) {
                    profInfo.add(line);
                }
            }
        }
        
        if (slInfo.isEmpty()) {
            addDefaultSLInfo();
        }
        if (taInfo.isEmpty()) {
            addDefaultTAInfo();
        }
        if (profInfo.isEmpty()) {
            addDefaultProfInfo();
        }
    }
    
    /**
     * Gets a random question for a specific keeper type
     */
    public String getRandomQuestion(KeeperType type) {
        switch (type) {
            case SECTION_LEADER:
                return getRandomItem(slQuestions);
            case TEACHING_ASSISTANT:
                return getRandomItem(taQuestions);
            case PROFESSOR:
                return getRandomItem(profQuestions);
            default:
                return "Default question";
        }
    }
    
    /**
     * Gets a random information for a specific keeper type
     */
    public String getRandomInfo(KeeperType type) {
        switch (type) {
            case SECTION_LEADER:
                return getRandomItem(slInfo);
            case TEACHING_ASSISTANT:
                return getRandomItem(taInfo);
            case PROFESSOR:
                return getRandomItem(profInfo);
            default:
                return "Default information";
        }
    }
    
    /**
     * Gets a random item from a list
     */
    private String getRandomItem(ArrayList<String> list) {
        if (list.isEmpty()) {
            return "Default item";
        }
        int index = random.nextInt(list.size());
        return list.get(index);
    }
    
    // Methods to add default content if files are empty
    private void addDefaultSLQuestions() {
        slQuestions.add("What is a class in Java?");
        slQuestions.add("What is an object?");
        slQuestions.add("What is a variable?");
        slQuestions.add("What is a method?");
        slQuestions.add("What is encapsulation?");
        slQuestions.add("What is the difference between int and Integer?");
        slQuestions.add("What is a constructor?");
        slQuestions.add("What are access modifiers?");
        slQuestions.add("What is inheritance?");
        slQuestions.add("How do you create an object in Java?");
    }
    
    private void addDefaultTAQuestions() {
        taQuestions.add("What is polymorphism?");
        taQuestions.add("What is method overriding?");
        taQuestions.add("What is method overloading?");
        taQuestions.add("What is an abstract class?");
        taQuestions.add("What is an interface?");
        taQuestions.add("What is the difference between an abstract class and an interface?");
        taQuestions.add("What is the Java Collections Framework?");
        taQuestions.add("What is a generic in Java?");
        taQuestions.add("What is exception handling?");
        taQuestions.add("What is a static method?");
    }
    
    private void addDefaultProfQuestions() {
        profQuestions.add("Explain the Singleton design pattern");
        profQuestions.add("What is the difference between composition and inheritance?");
        profQuestions.add("What is the Observer design pattern?");
        profQuestions.add("What is multithreading in Java?");
        profQuestions.add("What is synchronization in Java?");
        profQuestions.add("What are functional interfaces in Java?");
        profQuestions.add("What is a lambda expression?");
        profQuestions.add("What is the Stream API?");
        profQuestions.add("What is the Factory design pattern?");
        profQuestions.add("What is dependency injection?");
    }
    
    private void addDefaultSLInfo() {
        slInfo.add("A class is a blueprint for creating objects");
        slInfo.add("An object is an instance of a class");
        slInfo.add("Variables store data that can be modified");
        slInfo.add("Methods define the behavior of an object");
        slInfo.add("Encapsulation is hiding the internal state of an object");
        slInfo.add("int is a primitive type, Integer is a wrapper class");
        slInfo.add("A constructor initializes a newly created object");
        slInfo.add("Access modifiers control the visibility of classes, methods, and fields");
        slInfo.add("Inheritance allows a class to inherit properties from another class");
        slInfo.add("Objects are created using the 'new' keyword");
    }
    
    private void addDefaultTAInfo() {
        taInfo.add("Polymorphism allows objects to be treated as instances of their parent class");
        taInfo.add("Method overriding means replacing a method in a parent class");
        taInfo.add("Method overloading means having multiple methods with the same name but different parameters");
        taInfo.add("An abstract class cannot be instantiated and may contain abstract methods");
        taInfo.add("An interface is a contract that classes can implement");
        taInfo.add("Abstract classes can have state, interfaces cannot (before Java 8)");
        taInfo.add("The Collections Framework provides interfaces and classes for working with collections of objects");
        taInfo.add("Generics enable classes to work with any data type");
        taInfo.add("Exception handling deals with runtime errors");
        taInfo.add("A static method belongs to the class, not an instance");
    }
    
    private void addDefaultProfInfo() {
        profInfo.add("The Singleton pattern ensures a class has only one instance");
        profInfo.add("Composition means an object contains another object; inheritance means an object is another object");
        profInfo.add("The Observer pattern defines a one-to-many dependency between objects");
        profInfo.add("Multithreading allows concurrent execution of two or more parts of a program");
        profInfo.add("Synchronization prevents concurrent access to shared resources");
        profInfo.add("Functional interfaces have exactly one abstract method");
        profInfo.add("Lambda expressions implement functional interfaces concisely");
        profInfo.add("The Stream API processes sequences of elements");
        profInfo.add("The Factory pattern creates objects without specifying the exact class");
        profInfo.add("Dependency injection provides objects that an object needs");
    }
} 
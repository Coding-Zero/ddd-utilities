An unchecked exception which provides an simple way to build business related error messages.

### Raising an error

```
throw BusinessError.raise(BusinessError.DefaultErrors.NO_SUCH_ENTITY_FOUND)
        .message("No such student found for the given id.")       
        .details("schoolId", "1")
        .details("studentId", "123456")                    
        .build(); 
```
```
Exception in thread "main" com.codingzero.utilities.error.BusinessError: No such student found for the given id.
```

### Extending error types

```java
public enum Errors implements ErrorCode {

    DUPLICATE_STUDENT_ID("ERR_123", "DuplicateStudentId")
    ;

    private String code;
    private String brief;

    Errors(String code, String brief) {
        this.code = code;
        this.brief = brief;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getBrief() {
        return brief;
    }
}
```

### Throw Single Error
```
throw UnskippableError.SINGLE
        .code(Errors.DUPLICATE_STUDENT_ID)
        .message("Student id has been assigned.")
        .details("schoolId", "1")
        .details("studentId", "666666")                    
        .build(); 
```


### Throw Multiple Errors
```
throw UnskippableError.MULTIPLE
        .error(
            UnskippableError.SINGLE
                .code(Errors.DUPLICATE_STUDENT_ID)
                .message("Student id has been assigned.")
                .details("schoolId", "1")
                .details("studentId", "111")                    
                .build())
        .error(
            UnskippableError.SINGLE
                .code(Errors.DUPLICATE_STUDENT_ID)
                .message("Student id has been assigned.")
                .details("schoolId", "1")
                .details("studentId", "222")                    
                .build())                 
```
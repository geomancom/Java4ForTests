package ru.gb_dz;

public class Main {
    public static final String GET_URL = "https://testbase.atlassian.net/rest/api/3/issue/";
    public static final String GET_URL_KEY ="https://testbase.atlassian.net/rest/api/3/issue/{key}";
    public static final String GET_BODY = "{\n  \"fields\": {\n    \"summary\": \"позитивный запрос из документации\",\n    \"issuetype\": {\n      \"id\": \"10103\"\n    },\n    \"project\": {\n      \"id\": \"10302\"\n    }\n    }\n}\n";
}

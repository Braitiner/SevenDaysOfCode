package br.com.sevenDaysOfCode;


import java.net.URI;
import java.net.http.*;
import java.util.List;
import java.util.regex.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {


        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://imdb-api.com/en/API/Top250Movies/k_14ufi4ah")).GET().build();

        String json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).join();

        String[] moviesArray = parseJsonMovies(json);


        System.out.println(json);

        List<String> titles = parseTitles(moviesArray);
        titles.forEach(System.out::println);

        List<String> urlImages = parseUrlImage(moviesArray);
        urlImages.forEach(System.out::println);

        List<String> equipe = parseEquipe(moviesArray);
        equipe.forEach(System.out::println);

    }


    private static String[] parseJsonMovies(String json) {
        Matcher matcher = Pattern.compile(".*\\[(.*)\\].*").matcher(json);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("no match in " + json);
        }

        String[] moviesArray = matcher.group(1).split("\\},\\{");
        moviesArray[0] = moviesArray[0].substring(1);
        int last = moviesArray.length - 1;
        String lastString = moviesArray[last];
        moviesArray[last] = lastString.substring(0, lastString.length() - 1);
        return moviesArray;
    }

    private static List<String> parseTitles(String[] moviesArray) {
        return parseAttribute(moviesArray, 3);
    }

    private static List<String> parseUrlImage(String[] imageArray ){
        return  parseAttribute(imageArray, 5);
    }

    private static List<String> parseEquipe(String[] equipeArray){
        return parseAttribute(equipeArray, 6);
    }


    private static List<String> parseAttribute(String[] moviesArray, int pos) {
        return Stream.of(moviesArray)
                .map(e -> e.split("\",\"")[pos])
                .map(e -> e.split(":\"")[1])
                .map(e -> e.replaceAll("\"", ""))
                .collect(Collectors.toList());

    }

}










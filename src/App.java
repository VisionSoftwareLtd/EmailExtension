import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws Exception {
        String addresses = Files.readString(Path.of("sample.txt"));
        naiveApproach(addresses);
        withRegEx(addresses);
        withRegExStreams(addresses);
        usingMap(addresses);
    }

    private static void naiveApproach(String addresses) throws IOException {
        int count = 0;
        for (int i = 0; i < addresses.length() - 13; i++) {
            if (addresses.substring(i, i + 13).equals("@softwire.com")) {
                ++count;
            }
        }
        System.out.println(count);
    }

    private static void withRegEx(String addresses) throws IOException {
        Pattern pattern = Pattern.compile("\\s(\\S+@softwire.com)");
        Matcher matcher = pattern.matcher(addresses);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    private static void withRegExStreams(String addresses) throws IOException {
        Pattern pattern = Pattern.compile("\\s(\\S+@softwire.com)");
        Matcher matcher = pattern.matcher(addresses);
        matcher.results().map((result) -> result.group(1)).forEach(System.out::println);
    }

    private static void usingMap(String addresses) throws IOException {
        Map<String, Integer> emailMap = new HashMap<>();
        Pattern pattern = Pattern.compile("\\s(\\S+@(\\S+))\\s");
        Matcher matcher = pattern.matcher(addresses);
        matcher.results().forEach((matchResult) -> {
            String email = matchResult.group(1);
            String domain = matchResult.group(2);
            emailMap.put(domain, emailMap.containsKey(domain) ? emailMap.get(domain) + 1 : 1);
        });
        emailMap.entrySet()
            .stream()
            .sorted((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()))
            .forEach((entry) -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}

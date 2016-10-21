package tellh.com.gitclub.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tlh on 2016/8/24 :)
 */
public class Constant {
    public static final String URL_GITHUB = "https://api.github.com/";
    public static final String URL_EXPLORE = "http://trending.codehub-app.com/";
    public static final String URL_GANK = "http://gank.io/api/data/all/";
    public static final String URL_ARSENAL = "http://182.254.233.29/arsenal_api/";
    public static final int PER_PAGE = 10;
    public static final int PER_PAGE_NEWS = 15;
    public static final int PER_PAGE_GANK = 20;
    public static final int PER_PAGE_ARSENAL = 15;

    public interface SortType {
        SortType getBestMatch();

        String val();

        enum SortType_User implements SortType {
            BEST_MATCH("Best Match", null),
            FOLLOWERS("Followers", "followers"),
            REPOSITORIES("Repositories", "repositories"),
            JOINED("Joined", "joined");

            String display;
            String value;

            SortType_User(String key, String val) {
                display = key;
                value = val;
            }

            public static List<String> getDisplayStringList() {
                List<String> list = new ArrayList<>(4);
                for (SortType_User typeUser : SortType_User.values()) {
                    list.add(typeUser.display);
                }
                return list;
            }

            @Override
            public String val() {
                return value;
            }

            @Override
            public String toString() {
                return display;
            }

            @Override
            public SortType getBestMatch() {
                return SortType_User.BEST_MATCH;
            }

            public static SortType lookup(String key) {
                for (SortType_User typeUser : SortType_User.values()) {
                    if (!typeUser.display.equalsIgnoreCase(key))
                        continue;
                    return typeUser;
                }
                return SortType_Repo.BEST_MATCH;
            }

        }

        enum SortType_Repo implements SortType {
            BEST_MATCH("Best Match", null),
            STARS("Stars", "stars"),
            FORKS("Forks", "forks"),
            CREATED("Created", "created"),
            UPDATED("Updated", "updated");

            String display;
            String value;

            SortType_Repo(String key, String val) {
                display = key;
                value = val;
            }

            @Override
            public String val() {
                return value;
            }

            @Override
            public String toString() {
                return display;
            }

            @Override
            public SortType getBestMatch() {
                return SortType_Repo.BEST_MATCH;
            }

            public static List<String> getDisplayStringList() {
                List<String> list = new ArrayList<>(5);
                for (SortType_Repo typeRepo : SortType_Repo.values()) {
                    list.add(typeRepo.display);
                }
                return list;
            }

            public static List<String> getDisplayStringListForStarredRepo() {
                return Arrays.asList(CREATED.display, STARS.display, UPDATED.display);
            }

            public static SortType lookup(String key) {
                for (SortType_Repo typeRepo : SortType_Repo.values()) {
                    if (!typeRepo.display.equalsIgnoreCase(key))
                        continue;
                    return typeRepo;
                }
                return SortType_Repo.BEST_MATCH;
            }

        }

    }

    public enum Language {
        ALL("All", "+language:any language"),
        JAVA("Java", "+language:java"),
        CSS("CSS", "+language:css"),
        HTML("HTML", "+language:html"),
        JAVASCRIPT("Javascript", "+language:javascript"),
        OBJECTIVE_C("Objective-C", "+language:objective-c"),
        SWIFT("Swift", "+language:swift"),
        SHELL("Shell", "+language:bash"),
        C("C", "+language:c"),
        CPP("C++", "+language:cpp"),
        CSHARP("C#", "+language:csharp"),
        Python("Python", "+language:python"),
        Ruby("Ruby", "+language:Ruby"),
        Go("Go", "+language:Go"),
        PHP("PHP", "+language:PHP"),
        ASP("ASP", "+language:aspx-vb"),
        ANDROID("Android", " android+language:java"),
        WEB("Web", "+language:css+language:html+language:javascript");

        String value;
        String display;

        Language(String key, String val) {
            value = val;
            display = key;
        }

        public String val() {
            return value;
        }

        @Override
        public String toString() {
            return val();
        }

        public static List<String> getDisplayStringList() {
            List<String> list = new ArrayList<>(18);
            for (Language lang : Language.values()) {
                list.add(lang.display);
            }
            return list;
        }
    }

    public enum LangTrending {
        ALL("All", ""),
        JAVA("Java", "java"),
        CSS("CSS", "css"),
        HTML("HTML", "html"),
        JAVASCRIPT("Javascript", "javascript"),
        OBJECTIVE_C("Objective-C", "objective-c"),
        SWIFT("Swift", "swift"),
        SHELL("Shell", "bash"),
        C("C", "c"),
        CPP("C++", "cpp"),
        CSHARP("C#", "csharp"),
        Python("Python", "python"),
        Ruby("Ruby", "Ruby"),
        Go("Go", "go"),
        PHP("PHP", "PHP"),
        ASP("ASP", "aspx-vb");
        String value;
        String display;

        static String keyInMap = "language";

        public static String key() {
            return keyInMap;
        }

        LangTrending(String key, String val) {
            value = val;
            display = key;
        }

        @Override
        public String toString() {
            return value;
        }

        public static List<String> getDisplayStringList() {
            List<String> list = new ArrayList<>(15);
            for (LangTrending lang : LangTrending.values()) {
                list.add(lang.display);
            }
            return list;
        }
    }

    public enum Since {
        TODAY("Daily", "daily"),
        THIS_WEEK("Weekly", "weekly"),
        THIS_MONTH("Monthly", "monthly");

        private final String display;
        String value;

        static String keyInMap = "since";

        Since(String key, String val) {
            value = val;
            display = key;
        }

        public static String key() {
            return keyInMap;
        }

        @Override
        public String toString() {
            return value;
        }

        public static List<String> getDisplayStringList() {
            List<String> list = new ArrayList<>(3);
            for (Since since : Since.values()) {
                list.add(since.display);
            }
            return list;
        }
    }

}

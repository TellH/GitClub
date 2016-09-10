package tellh.com.gitclub.model.entity;

/**
 * Created by tlh on 2016/8/26 :)
 */
public class Trending {
    /**
     * owner : DevLight-Mobile-Agency
     * name : InfiniteCycleViewPager
     * url : https://api.github.com/repos/DevLight-Mobile-Agency/InfiniteCycleViewPager
     * avatarUrl : https://avatars.githubusercontent.com/u/18118313?v=3
     * description : Infinite cycle ViewPager with two-way orientation and interactive effect.
     * stars : 1113
     * forks : 143
     */

    private String owner;
    private String name;
    private String avatarUrl;
    private String description;
    private int stars;
    private int forks;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }
}

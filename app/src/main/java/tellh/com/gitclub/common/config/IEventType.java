package tellh.com.gitclub.common.config;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by tlh on 2016/9/20 :)
 */
public interface IEventType {
    String WatchEvent = "WatchEvent";
    String CreateEvent = "CreateEvent";
    String CommitCommentEvent = "CommitCommentEvent";
    String ForkEvent = "ForkEvent";
    String GollumEvent = "GollumEvent";
    String IssueCommentEvent = "IssueCommentEvent";
    String IssuesEvent = "IssuesEvent";
    String MemberEvent = "MemberEvent";
    String MembershipEvent = "MembershipEvent";
    String PublicEvent = "PublicEvent";
    String PullRequestEvent = "PullRequestEvent";
    String PullRequestReviewCommentEvent = "PullRequestReviewCommentEvent";
    String PushEvent = "PushEvent";
    String StatusEvent = "StatusEvent";
    String TeamAddEvent = "TeamAddEvent";
    String DeleteEvent = "DeleteEvent";
    String ReleaseEvent = "ReleaseEvent";
    String Unhandled = "Unhandled";

    @StringDef({WatchEvent, CreateEvent, CommitCommentEvent, ForkEvent, GollumEvent,
            IssueCommentEvent, IssuesEvent, MemberEvent, MembershipEvent, PublicEvent, PullRequestEvent,
            PullRequestReviewCommentEvent, PushEvent, StatusEvent, TeamAddEvent, DeleteEvent, ReleaseEvent, Unhandled})
    @Retention(RetentionPolicy.SOURCE)
    @interface EventType {
    }

}

package tellh.com.gitclub.presentation.view.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.common.utils.DateUtils;
import tellh.com.gitclub.common.utils.LogUtils;
import tellh.com.gitclub.common.utils.StringUtils;
import tellh.com.gitclub.common.wrapper.ImageLoader;
import tellh.com.gitclub.model.entity.Event;
import tellh.com.gitclub.presentation.contract.bus.RxBusPostman;
import tellh.com.gitclub.presentation.contract.bus.event.LaunchActivityEvent;

import static tellh.com.gitclub.common.config.IEventType.CommitCommentEvent;
import static tellh.com.gitclub.common.config.IEventType.CreateEvent;
import static tellh.com.gitclub.common.config.IEventType.DeleteEvent;
import static tellh.com.gitclub.common.config.IEventType.EventType;
import static tellh.com.gitclub.common.config.IEventType.ForkEvent;
import static tellh.com.gitclub.common.config.IEventType.GollumEvent;
import static tellh.com.gitclub.common.config.IEventType.IssueCommentEvent;
import static tellh.com.gitclub.common.config.IEventType.IssuesEvent;
import static tellh.com.gitclub.common.config.IEventType.MemberEvent;
import static tellh.com.gitclub.common.config.IEventType.MembershipEvent;
import static tellh.com.gitclub.common.config.IEventType.PublicEvent;
import static tellh.com.gitclub.common.config.IEventType.PullRequestEvent;
import static tellh.com.gitclub.common.config.IEventType.PullRequestReviewCommentEvent;
import static tellh.com.gitclub.common.config.IEventType.PushEvent;
import static tellh.com.gitclub.common.config.IEventType.ReleaseEvent;
import static tellh.com.gitclub.common.config.IEventType.WatchEvent;

public class NewsListAdapter extends BaseRecyclerAdapter<Event> {
    public NewsListAdapter(List<Event> items, Context context) {
        super(context, items);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_news;
    }

    @Override
    protected void bindData(RecyclerViewHolder holder, int position, final Event item) {
        holder.getView(R.id.item_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRepoActivity(item.getRepo().getName());
            }
        });
        ImageView imageView = holder.getImageView(R.id.iv_actor);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserInfoActivity(item.getActor().getLogin());
            }
        });
        ImageLoader.load(item.getActor().getAvatar_url(), imageView);
        holder.setText(R.id.tv_actor, item.getActor().getLogin())
                .setText(R.id.tv_time_ago, DateUtils.timeAgo(item.getCreated_at()))
                .setText(R.id.tv_repo, item.getRepo().getName());
        handleEventType(holder, item);
    }

    private void handleEventType(RecyclerViewHolder holder, Event item) {
        //reset it
        holder.setText(R.id.tv_desc, "");
        try {
            Event.PayloadEntity payload = item.getPayload();
            @EventType
            String type = item.getType();
            switch (type) {
                case WatchEvent:
                    holder.setText(R.id.tv_event, " starred");
                    break;
                case CreateEvent:
                    holder.setText(R.id.tv_event, "  created repo");
                    break;
                case CommitCommentEvent:
                    holder.setText(R.id.tv_event, " commented on")
                            .setText(R.id.tv_desc, payload.comment.body);
                    break;
                case ForkEvent:
                    holder.setText(R.id.tv_event, " forked")
                            .setText(R.id.tv_desc, StringUtils.append("to ", createReposSpan(payload.forkee.getFull_name())));
                    break;
                case GollumEvent:
                    holder.setText(R.id.tv_event, " created wiki page on");
                    break;
                case IssueCommentEvent:
                    holder.setText(R.id.tv_event, " commented")
                            .setText(R.id.tv_desc, StringUtils.append("on issue#",
                                    String.valueOf(payload.issue.number), ": ", payload.comment.body));
                    break;
                case IssuesEvent:
                    holder.setText(R.id.tv_event, StringUtils.append(payload.action, " issue"))
                            .setText(R.id.tv_desc, payload.issue.title);
                    break;
                case MemberEvent:
                    holder.setText(R.id.tv_event, "added collaborator to")
                            .setText(R.id.tv_desc, StringUtils.append("for ", payload.member.getLogin()));
                    break;
                case MembershipEvent:
                    holder.setText(R.id.tv_event, payload.action)
                            .setText(R.id.tv_desc, StringUtils.append("for ", payload.member.getLogin()));
                    break;
                case PublicEvent:
                    holder.setText(R.id.tv_event, " public");
                    break;
                case PullRequestEvent:
                    holder.setText(R.id.tv_event, StringUtils.append(payload.action, " pull request"))
                            .setText(R.id.tv_desc, StringUtils.append("title: ", payload.pull_request.title));
                    break;
                case PullRequestReviewCommentEvent:
                    holder.setText(R.id.tv_event, " commented on pull request")
                            .setText(R.id.tv_desc, payload.comment.body);
                    break;
                case PushEvent:
                    holder.setText(R.id.tv_event, " pushed to")
                            .setText(R.id.tv_desc, payload.ref);
                    break;
                case DeleteEvent:
                    holder.setText(R.id.tv_event, "  deleted repo");
                    break;
                case ReleaseEvent:
                    holder.setText(R.id.tv_event, "  released")
                            .setText(R.id.tv_desc, payload.release.body);
                    break;
                default:
                    holder.setText(R.id.tv_event, payload.action);
                    break;
            }
        } catch (IllegalArgumentException e) {
            LogUtils.e(e.getMessage());
        }
    }

    private SpannableString createReposSpan(final String showText) {
        SpannableString spanString = new SpannableString(showText);
        spanString.setSpan(showText, 0, showText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                gotoRepoActivity(showText);
            }
        }, 0, showText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    private void gotoRepoActivity(String repoFullName) {
        String[] pair = TextUtils.split(repoFullName, "/");
        if (pair.length != 2) {
            LogUtils.e("error in parse repo full name.");
            return;
        }
        Map<String, String> params = new HashMap<>(1);
        params.put(ExtraKey.USER_NAME, pair[0]);
        params.put(ExtraKey.REPO_NAME, pair[1]);
        RxBusPostman.postLaunchActivityEvent(params, LaunchActivityEvent.REPO_PAGE_ACTIVITY);
    }

    private void gotoUserInfoActivity(String user) {
        Map<String, String> params = new HashMap<>(1);
        params.put(ExtraKey.USER_NAME, user);
        RxBusPostman.postLaunchActivityEvent(params, LaunchActivityEvent.PERSONAL_HOME_PAGE_ACTIVITY);
    }
}

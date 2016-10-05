package tellh.com.gitclub.presentation.view.adapter.viewbinder;

import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tellh.nolistadapter.IListAdapter;
import com.tellh.nolistadapter.viewbinder.base.RecyclerViewBinder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.common.config.IEventType;
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

/**
 * Created by tlh on 2016/10/4 :)
 */

public class NewsListItemViewBinder extends RecyclerViewBinder<Event, NewsListItemViewBinder.ViewHolder> {
    @Override
    public ViewHolder provideViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(IListAdapter iListAdapter, ViewHolder holder, int i, final Event event) {
        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRepoActivity(event.getRepo().getName());
            }
        });
        holder.ivActor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserInfoActivity(event.getActor().getLogin());
            }
        });
        ImageLoader.load(event.getActor().getAvatar_url(), holder.ivActor);
        holder.tvActor.setText(event.getActor().getLogin());
        holder.tvTimeAgo.setText(DateUtils.timeAgo(event.getCreated_at()));
        holder.tvRepo.setText(event.getRepo().getName());
        handleEventType(holder, event);
    }

    private void handleEventType(ViewHolder holder, Event event) {
        //reset it
        holder.tvDesc.setText("");
        try {
            Event.PayloadEntity payload = event.getPayload();
            @IEventType.EventType
            String type = event.getType();
            switch (type) {
                case WatchEvent:
                    holder.tvEvent.setText(" starred");
                    break;
                case CreateEvent:
                    holder.tvEvent.setText("  created repo");
                    break;
                case CommitCommentEvent:
                    holder.tvEvent.setText(" commented on");
                    holder.tvDesc.setText(payload.comment.body);
                    break;
                case ForkEvent:
                    holder.tvEvent.setText(" forked");
                    holder.tvDesc.setText(StringUtils.append("to ", payload.forkee.getFull_name()));
                    break;
                case GollumEvent:
                    holder.tvEvent.setText(" created wiki page on");
                    break;
                case IssueCommentEvent:
                    holder.tvEvent.setText(" commented");
                    holder.tvDesc.setText(StringUtils.append("on issue#",
                            String.valueOf(payload.issue.number), ": ", payload.comment.body));
                    break;
                case IssuesEvent:
                    holder.tvEvent.setText(StringUtils.append(payload.action, " issue"));
                    holder.tvDesc.setText(payload.issue.title);
                    break;
                case MemberEvent:
                    holder.tvEvent.setText("added collaborator to");
                    holder.tvDesc.setText(StringUtils.append("for ", payload.member.getLogin()));
                    break;
                case MembershipEvent:
                    holder.tvEvent.setText(payload.action);
                    holder.tvDesc.setText(StringUtils.append("for ", payload.member.getLogin()));
                    break;
                case PublicEvent:
                    holder.tvEvent.setText(" public");
                    break;
                case PullRequestEvent:
                    holder.tvEvent.setText(StringUtils.append(payload.action, " pull request"));
                    holder.tvDesc.setText(StringUtils.append("title: ", payload.pull_request.title));
                    break;
                case PullRequestReviewCommentEvent:
                    holder.tvEvent.setText(" commented on pull request");
                    holder.tvDesc.setText(payload.comment.body);
                    break;
                case PushEvent:
                    holder.tvEvent.setText(" pushed to");
                    holder.tvDesc.setText(payload.ref);
                    break;
                case DeleteEvent:
                    holder.tvEvent.setText("  deleted repo");
                    break;
                case ReleaseEvent:
                    holder.tvEvent.setText("  released");
                    holder.tvDesc.setText(payload.release.body);
                    break;
                default:
                    holder.tvEvent.setText(payload.action);
                    break;
            }
        } catch (IllegalArgumentException e) {
            LogUtils.e(e.getMessage());
        }
    }


    @Override
    public int getItemLayoutId(IListAdapter iListAdapter) {
        return R.layout.item_news;
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

    class ViewHolder extends RecyclerViewBinder.ViewHolder {
        private CircleImageView ivActor;
        private TextView tvActor;
        private TextView tvEvent;
        private TextView tvRepo;
        private TextView tvDesc;
        private TextView tvTimeAgo;
        private CardView itemContainer;

        public ViewHolder(View rootView) {
            super(rootView);
            this.ivActor = (CircleImageView) rootView.findViewById(R.id.iv_actor);
            this.tvActor = (TextView) rootView.findViewById(R.id.tv_actor);
            this.tvEvent = (TextView) rootView.findViewById(R.id.tv_event);
            this.tvRepo = (TextView) rootView.findViewById(R.id.tv_repo);
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.tvTimeAgo = (TextView) rootView.findViewById(R.id.tv_time_ago);
            this.itemContainer = (CardView) rootView.findViewById(R.id.item_container);
        }
    }
}

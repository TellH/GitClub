package tellh.com.gitclub.presentation.contract.bus.event;

import tellh.com.gitclub.model.entity.ShowCase;

/**
 * Created by tlh on 2016/9/8 :)
 */
public class GetShowcaseDetailEvent {
    public ShowCase showCase;

    public GetShowcaseDetailEvent(ShowCase showCase) {
        this.showCase = showCase;
    }
}

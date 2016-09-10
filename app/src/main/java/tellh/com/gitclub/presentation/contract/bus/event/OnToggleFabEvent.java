//package tellh.com.gitclub.presentation.contract.bus.event;
//
///**
// * Created by tlh on 2016/9/6 :)
// */
//public class OnToggleFabEvent {
//    public enum FabType {
//        EXPLORE,
//        NEWS,
//        SEARCH,
//        HOME;
//
//        public static FabType lookUp(int pos) {
//            for (FabType fabType : FabType.values()) {
//                if (pos == fabType.ordinal())
//                    return fabType;
//            }
//            return EXPLORE;
//        }
//    }
//
//    public FabType target;
//    public boolean toShow;
//
//    public OnToggleFabEvent(boolean toShow, FabType target) {
//        this.toShow = toShow;
//    }
//}

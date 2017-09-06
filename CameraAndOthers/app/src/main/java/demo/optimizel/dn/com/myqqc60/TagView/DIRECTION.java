package demo.optimizel.dn.com.myqqc60.TagView;

/**
 * Created by dengguochuan on 2017/7/28.
 */

public enum DIRECTION {
    CENTER(0),RIGHT_TOP(1),RIGHT_TOP_LIFT(2),RIGHT_CENTRN(3),RIGHT_BOTTOM_LIFT(4),
    RIGHT_BOTTOM(5),LEFT_BOTTOM(6),LEFT_BOTTOM_LIFT(7),LEFT_CENTER(8),LEFT_TOP_LEFT(9),LEFT_TOP(10);
    private int value;
    DIRECTION(int value){
        this.value=value;
    }
    public int getValue(){
        return this.value;
    }
    public static DIRECTION valueOf(int value){
        switch (value){
            case 0:
                    return CENTER;

            case 1:
                return RIGHT_TOP;

            case 2:
                return RIGHT_BOTTOM_LIFT;

            case 3:
                return RIGHT_CENTRN;

            case 4:
                return RIGHT_BOTTOM_LIFT;

            case 5:
                return RIGHT_BOTTOM;

            case 6:
                return LEFT_BOTTOM;

            case 7:
                return LEFT_BOTTOM_LIFT;

            case 8:
                return LEFT_CENTER;

            case 9:
                return LEFT_TOP_LEFT;

            case 10:
                return LEFT_TOP;
            default:
                return CENTER;

        }

    }
}

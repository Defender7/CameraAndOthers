package demo.optimizel.dn.com.myqqc60.TagView.data;


import java.util.ArrayList;
import java.util.List;

import demo.optimizel.dn.com.myqqc60.TagView.DIRECTION;

/**
 * author: shell
 * date 2016/12/30 下午4:46
 **/
//没有网络请求，tags数据存在内存中
public class DataRepo {
    public static List<TagGroupModel> tagGroupList = new ArrayList<>();

    public static void initData() {
        TagGroupModel model1 = new TagGroupModel();

        TagGroupModel.Tag tag10 = new TagGroupModel.Tag();
        tag10.setDirection(DIRECTION.RIGHT_TOP.getValue());
        tag10.setLink("http://www.baidu.com");
        tag10.setName("温馨台灯，光线柔和");

        TagGroupModel.Tag tag11 = new TagGroupModel.Tag();
        tag11.setDirection(DIRECTION.RIGHT_CENTRN.getValue());
        tag11.setLink("http://www.baidu.com");
        tag11.setName("普吉岛");

        TagGroupModel.Tag tag12 = new TagGroupModel.Tag();
        tag12.setDirection(DIRECTION.RIGHT_BOTTOM.getValue());
        tag12.setLink("http://www.baidu.com");
        tag12.setName("¥1200");

        model1.getTags().add(tag10);
        model1.getTags().add(tag11);
        model1.getTags().add(tag12);
        model1.setPercentX(0.3f);
        model1.setPercentY(0.4f);

        tagGroupList.add(model1);


        TagGroupModel model2 = new TagGroupModel();

        TagGroupModel.Tag tag21 = new TagGroupModel.Tag();
        tag21.setDirection(DIRECTION.RIGHT_CENTRN.getValue());
        tag21.setLink("http://www.baidu.com");
        tag21.setName("装饰风扇");

        TagGroupModel.Tag tag22 = new TagGroupModel.Tag();
        tag22.setDirection(DIRECTION.RIGHT_BOTTOM.getValue());
        tag22.setLink("http://www.baidu.com");
        tag22.setName("¥600");

        model2.getTags().add(tag21);
        model2.getTags().add(tag22);
        model2.setPercentY(0.1f);
        model2.setPercentX(0.15f);

        tagGroupList.add(model2);

        TagGroupModel model3 = new TagGroupModel();

        TagGroupModel.Tag tag30 = new TagGroupModel.Tag();
        tag30.setDirection(DIRECTION.RIGHT_TOP_LIFT.getValue());
        tag30.setLink("http://www.baidu.com");
        tag30.setName("实木地板");

        TagGroupModel.Tag tag31 = new TagGroupModel.Tag();
        tag31.setDirection(DIRECTION.RIGHT_BOTTOM_LIFT.getValue());
        tag31.setLink("http://www.baidu.com");
        tag31.setName("欧诗漫");

        TagGroupModel.Tag tag32 = new TagGroupModel.Tag();
        tag32.setDirection(DIRECTION.LEFT_TOP_LEFT.getValue());
        tag32.setLink("http://www.baidu.com");
        tag32.setName("¥8800");

        TagGroupModel.Tag tag33 = new TagGroupModel.Tag();
        tag33.setDirection(DIRECTION.RIGHT_BOTTOM_LIFT.getValue());
        tag33.setLink("http://www.baidu.com");
        tag33.setName("很漂亮哦");

        model3.getTags().add(tag30);
        model3.getTags().add(tag31);
        model3.getTags().add(tag32);
        model3.getTags().add(tag33);
        model3.setPercentX(0.6f);
        model3.setPercentY(0.8f);

        tagGroupList.add(model3);
    }
}

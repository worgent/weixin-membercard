package weixin.guanjia.core.entity.message.resp;

/**
 * 音乐消息
 * @author Administrator
 *
 */
public class MusicMessageResp extends BaseMessageResp {
	// 音乐
    private weixin.guanjia.core.entity.message.resp.Music Music;

    public weixin.guanjia.core.entity.message.resp.Music getMusic() {
            return Music;
    }

    public void setMusic(weixin.guanjia.core.entity.message.resp.Music music) {
            Music = music;
    }
}

package cn.hz.ddbm.pc.profile;

import cn.hz.ddbm.pc.core.Profile;

public class StablePcService extends PcService {
    @Override
    public Profile profile() {
        return Profile.defaultOf();
    }
}

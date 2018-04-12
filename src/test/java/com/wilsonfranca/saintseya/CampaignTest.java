package com.wilsonfranca.saintseya;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by wilson on 11/04/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class CampaignTest {

    @Mock
    private Player player;

    @Mock
    private Quest quest;

    private Campaign campaign;

    @Before
    public void before() {
        this.campaign = new Campaign(player, quest);
    }

    @Test
    public void test_try_save_a_campaign_of_valid_player_and_is_ok() throws InterruptedException {

        Thread.sleep(3000);

        campaign.save();

        assertThat(campaign.getDateCreated()).isNotEqualTo(campaign.getSavedDate());
        assertThat(campaign.getSavedDate()).isAfter(campaign.getDateCreated());
        assertThat(campaign.getQuests()).isNotNull();
        assertThat(campaign.getPlayer()).isNotNull();

    }
}

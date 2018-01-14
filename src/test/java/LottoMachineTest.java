import lotto.domain.Lotto;
import lotto.domain.LottoMachine;
import lotto.domain.enums.LottoCorrectCount;
import lotto.util.LottoUtils;
import lotto.view.Input;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Optional.of;
import static lotto.domain.enums.LottoCorrectCount.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LottoMachineTest {

    @Test
    public void 금액에_맞는_개수인가() {
        LottoMachine lottoMachine = LottoMachine.init(Input.init("14000").getMoney());
        assertEquals(14, lottoMachine.getLottoCount());
    }

    @Test
    public void 입력된_개수에_맞는_로또가_생성되는가() {
        LottoMachine lottoMachine = LottoMachine.init(Input.init("14000").getMoney());
        assertEquals(14, lottoMachine.generateLottos().size());
    }

    @Test
    public void 당첨번호_통계결과가_일치하는가() {
        Map<LottoCorrectCount, Integer> resultMap = initResultMap();

        assertEquals(of(3).get(), resultMap.get(THREE));
        assertEquals(of(2).get(), resultMap.get(FOUR));
        assertEquals(of(1).get(), resultMap.get(SIX));
    }

    @Test
    public void 총수익이_올바른가() {
        Map<LottoCorrectCount, Integer> resultMap = initResultMap();
        assertEquals(2000115000, LottoUtils.getProfit(resultMap));
    }

    @Test
    public void 총수익률이_올바른가() {
        Map<LottoCorrectCount, Integer> resultMap = initResultMap();
        assertThat(LottoUtils.getProfitRate(LottoUtils.getProfit(resultMap), 6000), is(3.333525E7F));
    }

    private Map<LottoCorrectCount, Integer> initResultMap() {
        List<Lotto> lottos = new ArrayList<>();
        lottos.add(Lotto.generate(() -> new int[]{1, 2, 3, 40, 41, 42})); //THREE
        lottos.add(Lotto.generate(() -> new int[]{10, 20, 30, 4, 5, 6})); //THREE
        lottos.add(Lotto.generate(() -> new int[]{10, 2, 3, 4, 15, 16})); //THREE
        lottos.add(Lotto.generate(() -> new int[]{1, 2, 3, 4, 15, 16})); //FOUR
        lottos.add(Lotto.generate(() -> new int[]{11, 20, 3, 4, 5, 6})); //FOUR
        lottos.add(Lotto.generate(() -> new int[]{1, 2, 3, 4, 5, 6})); //SIX

        List<Integer> winNumbers = Input.init("1, 2, 3, 4, 5, 6").winNumbers();
        return LottoUtils.resultToMap(LottoMachine.getLottoResults(lottos, winNumbers));
    }
}

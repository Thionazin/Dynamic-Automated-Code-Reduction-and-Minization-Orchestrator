package com.dacrmo.cre.qa.suites;

import static com.dacrmo.cre.qa.framework.AssertionGovernanceService.*;

import com.dacrmo.cre.ai.model.FeatureExtractor;
import com.dacrmo.cre.ai.rules.WhitespaceSafetyRule;
import com.dacrmo.cre.lexical.JavaLexicalUtils;
import com.dacrmo.cre.qa.framework.SynergyTest;
import com.dacrmo.cre.qa.support.Fixture;
import com.dacrmo.cre.qa.support.FixtureRepository;
import com.dacrmo.cre.qa.support.ReductionFixtureFactory;
import com.dacrmo.cre.qa.support.ReductionRun;
import com.dacrmo.cre.qa.support.TemporaryWorkspace;
import java.util.Arrays;

// keeps the README technically true: if one of these fails, a marketing claim has become false
public class ReadmeClaimComplianceTest
{
    @SynergyTest("README: perfect agreement with ground truth in a single training run")
    public void perfectAgreementInOneRun() throws Exception
    {
        for (Fixture fixture : new FixtureRepository().fixtures())
        {
            ReductionRun run = ReductionFixtureFactory.reduce(fixture.getSource());
            assertEquals(0, run.getOverruledDecisions(), fixture.getClassName() + " had overruled AI decisions");
            assertTrue(run.getAcceptedDecisions() > 0, fixture.getClassName() + " made no AI decisions at all");
        }
    }

    @SynergyTest("README: every AI decision that ships is correct, even from a deliberately wrong network")
    public void shippedDecisionsAreAlwaysCorrect() throws Exception
    {
        WhitespaceSafetyRule rule = new WhitespaceSafetyRule();
        double[] sabotaged = new double[FeatureExtractor.FEATURE_COUNT];
        Arrays.fill(sabotaged, 1000);
        try (TemporaryWorkspace emptyCorpus = new TemporaryWorkspace())
        {
            for (Fixture fixture : new FixtureRepository().fixtures())
            {
                String output = ReductionFixtureFactory.reduce(fixture.getSource(), sabotaged, emptyCorpus.getRoot().toFile()).getOutput();
                for (int index : JavaLexicalUtils.spacesOutsideLiterals(output))
                {
                    assertTrue(rule.needsSpace(output.charAt(index - 1), output.charAt(index + 1)),
                            fixture.getClassName() + " shipped an unnecessary space at " + index);
                }
                assertEquals(ReductionFixtureFactory.reduce(fixture.getSource()).getOutput(), output,
                        fixture.getClassName() + " output depended on the network's starting state");
            }
        }
    }

    @SynergyTest("README: fully deterministic, the same input yields the same output on every run")
    public void reductionIsDeterministic() throws Exception
    {
        for (Fixture fixture : new FixtureRepository().fixtures())
        {
            String first = ReductionFixtureFactory.reduce(fixture.getSource()).getOutput();
            for (int run = 0; run < 3; run++)
            {
                assertEquals(first, ReductionFixtureFactory.reduce(fixture.getSource()).getOutput(), fixture.getClassName() + " was not deterministic");
            }
        }
    }
}

package com.dacrmo.cre.qa.suites;

import static com.dacrmo.cre.qa.framework.AssertionGovernanceService.*;

import com.dacrmo.cre.lexical.JavaLexicalUtils;
import com.dacrmo.cre.qa.framework.SynergyTest;
import com.dacrmo.cre.qa.support.Fixture;
import com.dacrmo.cre.qa.support.FixtureRepository;
import com.dacrmo.cre.qa.support.ReductionFixtureFactory;
import java.nio.file.Path;

public class OneLineInvariantTest
{
    private final FixtureRepository repository = new FixtureRepository();

    @SynergyTest("every fixture reduces to exactly one line")
    public void fixturesReduceToOneLine() throws Exception
    {
        for (Fixture fixture : repository.fixtures())
        {
            String output = ReductionFixtureFactory.reduce(fixture.getSource()).getOutput();
            assertSingleLine(output, fixture.getClassName() + " was not reduced to one line");
            assertFalse(output.isEmpty(), fixture.getClassName() + " reduced to nothing");
        }
    }

    @SynergyTest("every production source file reduces to exactly one line")
    public void productionSourcesReduceToOneLine() throws Exception
    {
        for (Path path : repository.productionSources())
        {
            String output = ReductionFixtureFactory.reduce(JavaLexicalUtils.read(path.toFile())).getOutput();
            assertSingleLine(output, path + " was not reduced to one line");
        }
    }

    @SynergyTest("CRLF and CR line endings are harmonized away too")
    public void foreignLineEndingsReduceToOneLine()
    {
        String output = ReductionFixtureFactory.reduce("class A {\r\n  int x;\r  int y;\n}\r\n").getOutput();
        assertEquals("class A{int x;int y;}", output, "line endings from every jurisdiction must collapse");
    }
}

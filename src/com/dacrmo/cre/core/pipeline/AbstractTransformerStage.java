package com.dacrmo.cre.core.pipeline;

import com.dacrmo.cre.api.Transformer;

public abstract class AbstractTransformerStage implements Transformer
{
    public final String transform(String src)
    {
        if (src == null) throw new IllegalArgumentException(getClass().getSimpleName() + " received no source");
        return doTransform(src);
    }

    protected abstract String doTransform(String src);
}

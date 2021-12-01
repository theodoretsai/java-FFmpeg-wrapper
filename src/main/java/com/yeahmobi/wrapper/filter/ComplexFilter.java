package com.yeahmobi.wrapper.filter;

import com.yeahmobi.wrapper.filterable.AVParam;
import com.yeahmobi.wrapper.filterable.AudioParam;
import com.yeahmobi.wrapper.filterable.VideoParam;
import com.yeahmobi.wrapper.filterable.VisualParam;
import com.yeahmobi.wrapper.filter.visual.FilterChain;

import java.util.ArrayList;
import java.util.List;


/**
 * @author theodore.tsai
 * @descrption Cmplex Filter Class
 * Generates the complex filter string to be added to the command arguments.
 * end result: -filter_complex "[filter_string]"
 * @date 20/09/2021
 */
public class ComplexFilter {

    private final List<Filter> filters;

    /**
     * 默认 Constructor
     */
    public ComplexFilter() {
        this.filters = new ArrayList<>();
    }

    /**
     * 生成复杂过滤器中的命令
     * 不包含 -filter_complex, 加到命令生成器后生命令成器会自动生成完整过滤器命令
     *
     * @return the string
     */
    public String generateComplexFilter() {

        StringBuilder command = new StringBuilder();
        for (Filter Filter : filters) {
            command.append(Filter.generateFilter());
        }
        return command.toString();
    }

    /**
     * 增加过滤器
     *
     * @param Filter 过滤器
     * @return this, 支持连续添加
     */
    private ComplexFilter addFilter(Filter Filter) {
        if (!this.filters.isEmpty()) {
            this.filters.add(new FilterChain(false));
        }
        this.filters.add(Filter);
        return this;
    }

    /**
     * Chain filter complex filter.
     *
     * @param Filter the f fmpeg filter
     * @return the complex filter
     */
    @Deprecated
    public ComplexFilter chainFilter(Filter Filter) {
        if (!this.filters.isEmpty()) {
            this.filters.add(new FilterChain(true));
        }
        this.filters.add(Filter);
        return this;
    }

    public ComplexFilter addScaleFilter(VisualParam input, VisualParam output, Integer width, Integer height, Boolean forceAspectRatio, Boolean isIncrease) {
        return this.addFilter(FilterFactory.buildScaleFilter(input, output, width, height, forceAspectRatio, isIncrease));
    }

    public ComplexFilter addCropFilter(VisualParam input, VisualParam output, Integer width, Integer height) {
        return this.addFilter(FilterFactory.buildCropFilter(input, output, width, height));
    }

    public ComplexFilter addPadFilter(VisualParam input, VisualParam output, Integer width, Integer height, Integer horizontalOffset, Integer verticalOffset) {
        return this.addFilter(FilterFactory.buildPadFilter(input, output, width, height, horizontalOffset, verticalOffset));
    }

    public ComplexFilter addOverLayFilter(VisualParam main, VisualParam overlay, VisualParam output, Integer horizontalOffset, Integer verticalOffset, Float start, Float end) {
        return this.addFilter(FilterFactory.buildOverlayFilter(main, overlay, output, horizontalOffset, verticalOffset, start, end));
    }

    public ComplexFilter addOverLayFilter(VisualParam main, VisualParam overlay, VisualParam output, Integer horizontalOffset, Integer verticalOffset) {
        return this.addFilter(FilterFactory.buildOverlayFilter(main, overlay, output, horizontalOffset, verticalOffset));
    }

    public ComplexFilter addConcatFilter(List<VideoParam> videoInputs, List<AudioParam> audioInputs, AVParam outputs) {
        return this.addFilter(FilterFactory.buildConcatFilter(videoInputs,audioInputs, outputs));
    }

    public ComplexFilter addDarFilter(VisualParam input, VisualParam output, String dar) {
        return this.addFilter(FilterFactory.buildDarFilter(input, output, dar));
    }

    public ComplexFilter addAFadeFilter(AudioParam input, AudioParam output, Boolean isIn, Float start, Float duration) {
        return this.addFilter(FilterFactory.buildAfadeFilter(input, output, start, duration, isIn));
    }

}

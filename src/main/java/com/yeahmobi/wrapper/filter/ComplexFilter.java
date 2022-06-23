package com.yeahmobi.wrapper.filter;

import com.yeahmobi.wrapper.filter.audio.AFadeFilter;
import com.yeahmobi.wrapper.filter.custom.CustomFilter;
import com.yeahmobi.wrapper.filter.params.*;
import com.yeahmobi.wrapper.filter.visual.*;
import com.yeahmobi.wrapper.filterable.*;
import com.yeahmobi.wrapper.filterable.results.AVParam;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * @author theodore.tsai
 * @descrption Cmplex Filter Class
 * Generates the complex filter string to be added to the command arguments.
 * end result: -filter_complex "[filter_string]"
 * @date 20/09/2021
 */
@Getter
public class ComplexFilter {

    private final List<Filter> filters;

    /**
     * 默认 Constructor
     */
    public ComplexFilter() {
        this.filters = new LinkedList<>();
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
    public ComplexFilter addFilter(Filter Filter) {
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
        return this.addFilter(new ScaleFilter(input, output, new ScaleParam(width, height, forceAspectRatio, isIncrease)));
    }

    public ComplexFilter addCropFilter(VisualParam input, VisualParam output, Integer width, Integer height) {
        return this.addFilter(new CropFilter(input, output, new CropParam(width, height)));
    }

    public ComplexFilter addPadFilter(VisualParam input, VisualParam output, Integer width, Integer height, Integer horizontalOffset, Integer verticalOffset) {
        return this.addFilter(new PadFilter(input, output, new PadParam(width, height, horizontalOffset, verticalOffset)));
    }

    public ComplexFilter addOverLayFilter(VisualParam main, VisualParam overlay, VisualParam output, Integer horizontalOffset, Integer verticalOffset, Float start, Float end) {
        return this.addFilter(new OverlayFilter(main,overlay, output, new OverlayParam(horizontalOffset, verticalOffset), new TimeLineParam(start, end)));
    }

    public ComplexFilter addOverLayFilter(VisualParam main, VisualParam overlay, VisualParam output, Integer horizontalOffset, Integer verticalOffset) {
        return this.addFilter(new OverlayFilter(main,overlay, output, new OverlayParam(horizontalOffset, verticalOffset), null));
    }

    public ComplexFilter addConcatFilter(List<VideoParam> concatList, List<AudioParam> audioList, AVParam output) {
        return this.addFilter(new SimpleConcatFilter(concatList, audioList, output));
    }

    public ComplexFilter addDarFilter(VisualParam input, VisualParam output, String dar) {
        return this.addFilter(new DarFilter(input, output, new DarParam(dar)));
    }

    public ComplexFilter addAFadeFilter(AudioParam input, AudioParam output, Boolean isIn, Float start, Float duration) {
        return this.addFilter(new AFadeFilter(input, output, new AFadeParam(isIn, start, duration, null)));
    }

    public ComplexFilter addCustomFilter(Filterable input, Filterable output, String filterName) {
        return this.addFilter(new CustomFilter(input, output, filterName, new ArrayList<>()));
    }
}

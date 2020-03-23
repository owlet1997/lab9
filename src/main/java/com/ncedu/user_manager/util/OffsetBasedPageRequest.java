package com.ncedu.user_manager.util;

import com.ncedu.user_manager.exception.BaseException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ncedu.user_manager.service.error.Error.*;

@EqualsAndHashCode
@ToString
public class OffsetBasedPageRequest implements Pageable, Serializable {

    private static final long serialVersionUID = -3349678972974618050L;

    private static final int DEFAULT_START = 1;
    private static final int DEFAULT_END = 10;

    public static final String CONTENT_RANGE_HEADER = "Content-Range";

    private static final Pattern RANGE_PATTERN = Pattern.compile("^([0-9]+)-([0-9]+)$");
    private static final Pattern SORT_PATTERN = Pattern.compile("^([^:]+)(:(asc|desc))?$");

    private final int limit;
    private final long offset;
    private final Sort sort;

    private OffsetBasedPageRequest(long offset, int limit, Sort sort) {
        if(offset < 0) {
            throw new BaseException(INVALID_OFFSET);
        }
        if(limit < 1) {
            throw new BaseException(INVALID_LIMIT);
        }

        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    public OffsetBasedPageRequest(String range, List<String> sorting) {
        int start = DEFAULT_START;
        int end = DEFAULT_END;

        if (!StringUtils.isEmpty(range)) {
            Matcher matcher = RANGE_PATTERN.matcher(range);
            if (matcher.find()) {
                start = Integer.parseInt(matcher.group(1));
                end = Integer.parseInt(matcher.group(2));
            } else {
                throw new BaseException(INVALID_RANGE);
            }
        }

        offset = start - 1;
        if(offset < 0) {
            throw new BaseException(INVALID_OFFSET);
        }

        limit = end - start + 1;
        if(limit < 1) {
            throw new BaseException(INVALID_LIMIT);
        }

        if (CollectionUtils.isEmpty(sorting)) {
            sort = Sort.unsorted();
        } else {
            sort = Sort.by(sorting.stream().map(s -> {
                Matcher sortMatcher = SORT_PATTERN.matcher(s);
                if (sortMatcher.find()) {
                    String property = sortMatcher.group(1);
                    Sort.Direction direction =
                            Optional.ofNullable(sortMatcher.group(3))
                                    .map(Sort.Direction::fromString)
                                    .orElse(Sort.Direction.ASC);
                    return new Sort.Order(direction, property);
                } else {
                    throw new BaseException(INVALID_SORT);
                }
            }).collect(Collectors.toList()));
        }
    }

    @Override
    public int getPageNumber() {
        return Math.toIntExact(offset / limit);
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetBasedPageRequest(getOffset() + getPageSize(), getPageSize(), getSort());
    }

    public OffsetBasedPageRequest previous() {
        return hasPrevious() ? new OffsetBasedPageRequest(getOffset() - getPageSize(), getPageSize(), getSort()) : this;
    }


    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(0, getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}

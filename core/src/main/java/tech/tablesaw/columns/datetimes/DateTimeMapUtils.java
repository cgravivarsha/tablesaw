/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.tablesaw.columns.datetimes;

import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.NumberColumn;
import tech.tablesaw.columns.Column;
import tech.tablesaw.columns.numbers.NumberColumnFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

import static tech.tablesaw.columns.datetimes.PackedLocalDateTime.*;

public interface DateTimeMapUtils extends Column {

    default NumberColumn differenceInMilliseconds(DateTimeColumn column2) {
        return difference(column2, ChronoUnit.MILLIS);
    }

    default NumberColumn differenceInSeconds(DateTimeColumn column2) {
        return difference(column2, ChronoUnit.SECONDS);
    }

    default NumberColumn differenceInMinutes(DateTimeColumn column2) {
        return difference(column2, ChronoUnit.MINUTES);
    }

    default NumberColumn differenceInHours(DateTimeColumn column2) {
        return difference(column2, ChronoUnit.HOURS);
    }

    default NumberColumn differenceInDays(DateTimeColumn column2) {
        return difference(column2, ChronoUnit.DAYS);
    }

    default NumberColumn differenceInYears(DateTimeColumn column2) {
        return difference(column2, ChronoUnit.YEARS);
    }

    default NumberColumn difference(DateTimeColumn column2, ChronoUnit unit) {
        NumberColumn newColumn = NumberColumn.create(name() + " - " + column2.name());

        for (int r = 0; r < size(); r++) {
            long c1 = this.getLongInternal(r);
            long c2 = column2.getLongInternal(r);
            if (c1 == DateTimeColumn.MISSING_VALUE || c2 == DateTimeColumn.MISSING_VALUE) {
                newColumn.append(DateTimeColumn.MISSING_VALUE);
            } else {
                newColumn.append(difference(c1, c2, unit));
            }
        }
        return newColumn;
    }

    default long difference(long packedLocalDateTime1, long packedLocalDateTime2, ChronoUnit unit) {
        LocalDateTime value1 = asLocalDateTime(packedLocalDateTime1);
        LocalDateTime value2 = asLocalDateTime(packedLocalDateTime2);
        return unit.between(value1, value2);
    }

    default NumberColumn hour() {
        NumberColumn newColumn = NumberColumn.create(name() + "[" + "hour" + "]");
        for (int r = 0; r < size(); r++) {
            long c1 = getLongInternal(r);
            if (c1 != DateTimeColumn.MISSING_VALUE) {
                newColumn.append(getHour(c1));
            } else {
                newColumn.append(NumberColumn.MISSING_VALUE);
            }
        }
        return newColumn;
    }

    default NumberColumn minuteOfDay() {
        NumberColumn newColumn = NumberColumn.create(name() + "[" + "minute-of-day" + "]");
        for (int r = 0; r < size(); r++) {
            long c1 = getLongInternal(r);
            if (c1 != DateTimeColumn.MISSING_VALUE) {
                newColumn.append((short) getMinuteOfDay(c1));
            } else {
                newColumn.append(NumberColumn.MISSING_VALUE);
            }
        }
        return newColumn;
    }

    default NumberColumn secondOfDay() {
        NumberColumn newColumn = NumberColumn.create(name() + "[" + "second-of-day" + "]");
        for (int r = 0; r < size(); r++) {
            long c1 = getLongInternal(r);
            if (c1 != DateTimeColumn.MISSING_VALUE) {
                newColumn.append(getSecondOfDay(c1));
            } else {
                newColumn.append(NumberColumn.MISSING_VALUE);
            }
        }
        return newColumn;
    }

    /**
     * Returns a column containing integers representing the nth group (0-based) that a date falls into.
     *
     * Example:     When Unit = ChronoUnit.DAY and n = 5, we form 5 day groups. a Date that is 2 days after the start
     * is assigned to the first ("0") group. A day 7 days after the start is assigned to the second ("1") group.
     *
     * @param unit  A ChronoUnit greater than or equal to a day
     * @param n     The number of units in each group.
     * @param start The starting point of the first group; group boundaries are offsets from this point
     */
    default NumberColumn timeWindow(ChronoUnit unit, int n, LocalDateTime start) {
        String newColumnName = "" +  n + " " + unit.toString() + " window [" + name() + "]";
        long packedStartDate = pack(start);
        NumberColumn numberColumn = NumberColumn.create(newColumnName, size());
        for (int i = 0; i < size(); i++) {
            long packedDate = getLongInternal(i);
            int result;
            switch (unit) {

                // TODO(lwhite): Add support for hours and minutes

                case DAYS:
                    result = daysUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                case WEEKS:
                    result = weeksUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                case MONTHS:
                    result = monthsUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                case YEARS:
                    result = yearsUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                default:
                    throw new UnsupportedTemporalTypeException("The ChronoUnit " + unit + " is not supported for timeWindows on dates");
            }
        }
        numberColumn.setPrintFormatter(NumberColumnFormatter.ints());
        return numberColumn;
    }

    default NumberColumn timeWindow(ChronoUnit unit, int n) {
        return timeWindow(unit, n, min());
    }

    LocalDateTime get(int r);

    long getLongInternal(int r);

    LocalDateTime min();
}

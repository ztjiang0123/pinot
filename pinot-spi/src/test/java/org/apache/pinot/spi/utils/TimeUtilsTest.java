/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pinot.spi.utils;

import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class TimeUtilsTest {

  @Test
  public void testConvertDateTimeToMillis() {
    assertEquals((long) TimeUtils.convertTimestampToMillis("2022-08-09T12:31:38.222Z"), 1660048298222L);
    Assert.assertThrows(IllegalArgumentException.class, () -> TimeUtils
        .convertTimestampToMillis("2022-08-09X12:31:38.222Z"));
    assertEquals((long) TimeUtils.convertTimestampToMillis(null), 0L);
  }

  @Test
  public void testIsPeriodValid() {
    Assert.assertTrue(TimeUtils.isPeriodValid("2d"));
    Assert.assertTrue(TimeUtils.isPeriodValid(""));
    Assert.assertTrue(TimeUtils.isPeriodValid("1m"));
    Assert.assertTrue(TimeUtils.isPeriodValid("2h"));
    Assert.assertTrue(TimeUtils.isPeriodValid("-2h"));
    Assert.assertTrue(TimeUtils.isPeriodValid("-2m"));
    Assert.assertTrue(TimeUtils.isPeriodValid("-4d"));
    Assert.assertFalse(TimeUtils.isPeriodValid(null));
  }

  @Test
  public void testIsValidTimeIntervalBoundaries() {
    long min = TimeUtils.VALID_MIN_TIME_MILLIS;
    long max = TimeUtils.VALID_MAX_TIME_MILLIS;

    // Interval exactly at the valid boundaries is valid (endpoints are inclusive).
    Assert.assertTrue(TimeUtils.isValidTimeInterval(new Interval(min, max, DateTimeZone.UTC)));
    // Zero-length intervals sitting on each boundary are valid.
    Assert.assertTrue(TimeUtils.isValidTimeInterval(new Interval(min, min, DateTimeZone.UTC)));
    Assert.assertTrue(TimeUtils.isValidTimeInterval(new Interval(max, max, DateTimeZone.UTC)));

    // Start just below the minimum is invalid.
    Assert.assertFalse(TimeUtils.isValidTimeInterval(new Interval(min - 1, max, DateTimeZone.UTC)));
    // End just above the maximum is invalid.
    Assert.assertFalse(TimeUtils.isValidTimeInterval(new Interval(min, max + 1, DateTimeZone.UTC)));
    // Entirely out of range on either side is invalid.
    Assert.assertFalse(TimeUtils.isValidTimeInterval(new Interval(min - 2, min - 1, DateTimeZone.UTC)));
    Assert.assertFalse(TimeUtils.isValidTimeInterval(new Interval(max + 1, max + 2, DateTimeZone.UTC)));
  }

  @Test
  public void testIsTimestampValid() {
    Assert.assertTrue(TimeUtils.isTimestampValid("2022-08-09T12:31:38.222Z"));
    Assert.assertTrue(TimeUtils.isTimestampValid("2020-02-08T09:30:26Z"));
    Assert.assertFalse(TimeUtils.isTimestampValid("2020-02-08T09"));
    Assert.assertFalse(TimeUtils.isTimestampValid(""));
    Assert.assertFalse(TimeUtils.isTimestampValid("2022-08-09"));
    Assert.assertFalse(TimeUtils.isTimestampValid("2022-08"));
    Assert.assertFalse(TimeUtils.isTimestampValid(null));
  }
}

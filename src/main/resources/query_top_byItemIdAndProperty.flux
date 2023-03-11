from(bucket: "%s")
  |> range(start: 0)
  |> filter(fn: (r) => r._measurement == "numeric_value" and r.itemId == "%s" and r.property == "%s")
  |> top(n:100, columns:["_time"])
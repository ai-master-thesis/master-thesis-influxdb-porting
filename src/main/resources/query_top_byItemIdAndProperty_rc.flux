from(bucket: "%s")
  |> range(start: 0)
  |> filter(fn: (r) => r.itemId == "%s" and r.property == "%s")
  |> top(n:%s, columns:["_time"])
from(bucket: "sensor_data_52")
  |> range(start: 0)
  |> filter(fn: (r) => r._measurement == "numeric_value" and r.itemId == "1233" and r.property == "Photocell")
  |> top(n:100, columns:["_time"])
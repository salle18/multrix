(ns multrix.game.blocks)

(def tetronimos-bit-positions (map #(bit-shift-right 0x8000 %) (range 16)))

(def tetrominos
  [{:type :i :positions [0x0F00, 0x2222, 0x00F0, 0x4444] :color "cyan"}
   {:type :j :positions [0x44C0, 0x8E00, 0x6440, 0x0E20] :color "blue"}
   {:type :l :positions [0x4460, 0x0E80, 0xC440, 0x2E00] :color "orange"}
   {:type :o :positions [0xCC00, 0xCC00, 0xCC00, 0xCC00] :color "yellow"}
   {:type :s :positions [0x06C0, 0x8C40, 0x6C00, 0x4620] :color "green"}
   {:type :t :positions [0x0E40, 0x4C40, 0x4E00, 0x4640] :color "purple"}
   {:type :z :positions [0x0C60, 0x4C80, 0xC600, 0x2640] :color "red"}])

(def tetrominos-bag (vec (shuffle (flatten (map #(repeat 4 %) tetrominos)))))

package models

type Product struct {
	ID          int     `json:"id"`
	Name        string  `json:"name"`
	Description string  `json:"description"`
	Image       string  `json:"image"`
	Category    string  `json:"cattegory"`
	Price       float64 `json:"price"`
}

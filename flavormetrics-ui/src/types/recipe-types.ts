import type { UUID } from "crypto";

declare enum DifficultyType {
  EASY,
  MEDIUM,
  HARD,
}

declare enum UnitType {
  grams,
  kilograms,
  milliliters,
  liters,
  pieces,
  tablespoons,
  teaspoons,
  cups,
  cloves,
  slices,
}

export interface TagDto {
  name: string;
}

export interface IngredientDto {
  readonly name: string;
  readonly quantity: number;
  readonly unit: UnitType;
}

export interface RatingDto {
  readonly recipeId: UUID;
  readonly user: string;
  readonly score: number;
}

export interface AllergyDto {
  readonly name: string;
  readonly description: string;
}

export interface RecipeDto {
  readonly id: UUID;
  readonly name: string;
  readonly user: string;
  readonly instructions: string;
  readonly imageUrl: string | undefined;
  readonly prepTimeMinutes: number | undefined;
  readonly cookTimeMinutes: number | undefined;
  readonly difficulty: DifficultyType;
  readonly estimatedCalories: number | undefined;
  readonly averageRating: number | undefined;
  readonly createdAt: Date;
  readonly updatedAt: Date;
  readonly tags: TagDto[];
  readonly ingredients: IngredientDto[];
  readonly ratings: RatingDto[];
  readonly allergies: AllergyDto[];
}

export interface DataWithPagination<T> {
  readonly data: T;
  readonly pageSize: number;
  readonly pageNumber: number;
  readonly totalPages: number;
}
